package sg.ncl.service.upload.logic;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.upload.DirectoryProperties;
import sg.ncl.service.upload.data.ResumableEntity;
import sg.ncl.service.upload.data.ResumableStorage;
import sg.ncl.service.upload.domain.UploadService;
import sg.ncl.service.upload.domain.UploadStatus;
import sg.ncl.service.upload.util.HttpUtils;
import sg.ncl.service.upload.web.ResumableInfo;

import javax.inject.Inject;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by dcsjnh on 11/24/2016.
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final ResumableStorage storage;
    private final DirectoryProperties properties;

    @Inject
    UploadServiceImpl(final ResumableStorage storage, final DirectoryProperties properties) {
        this.storage = storage;
        this.properties = properties;
    }

    @Override
    public UploadStatus checkChunk(String resumableIdentifier, int resumableChunkNumber) {
        ResumableEntity entity = storage.get(resumableIdentifier);
        if (entity != null && entity.uploadedChunks.contains(new ResumableEntity.ResumableChunkNumber(resumableChunkNumber))) {
            return UploadStatus.UPLOADED;
        } else {
            return  UploadStatus.NOT_FOUND;
        }
    }

    @Override
    public UploadStatus addChunk(ResumableInfo resumableInfo, int resumableChunkNumber, String subDirKey, String preDir) {
        Path path = HttpUtils.getPath(properties, subDirKey, preDir);
        //if directory exists?
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new BadRequestException();
            }
        }

        //Here we add a ".temp" to every upload file to indicate NON-FINISHED
        String resumableFilePath = path.toString() + "/" + resumableInfo.getResumableFilename() + ".temp";

        ResumableEntity entity = storage.get(
                resumableInfo.getResumableChunkSize(),
                resumableInfo.getResumableTotalSize(),
                resumableInfo.getResumableIdentifier(),
                resumableInfo.getResumableFilename(),
                resumableInfo.getResumableRelativePath(),
                resumableFilePath);

        if (!entity.valid()) {
            storage.remove(entity);
            log.error("Invalid resumable");
            throw new BadRequestException();
        }

        writeChunk(entity, resumableChunkNumber, resumableInfo);

        //Mark as uploaded.
        entity.uploadedChunks.add(new ResumableEntity.ResumableChunkNumber(resumableChunkNumber));
        if (entity.checkIfUploadFinished()) { //Check if all chunks uploaded, and change filename
            storage.remove(entity);
            return UploadStatus.FINISHED;
        } else {
            return UploadStatus.UPLOAD;
        }
    }

    private void writeChunk(ResumableEntity entity, int resumableChunkNumber, ResumableInfo resumableInfo) {
        try (RandomAccessFile raf = new RandomAccessFile(entity.resumableFilePath, "rw")) {
            //Seek to position
            log.info("resumableChunkNumber: " + resumableChunkNumber + " resumableChunkSize: " + entity.resumableChunkSize);
            raf.seek((resumableChunkNumber - 1) * (long) entity.resumableChunkSize);
            //Save to file
            byte[] bytes = Base64.decodeBase64(resumableInfo.getResumableChunk());
            raf.write(bytes);
        } catch (Exception e) {
            log.error("Error saving chunk: {}", e.getMessage());
            throw new BadRequestException();
        }
    }

}
