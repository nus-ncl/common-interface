package sg.ncl.service.upload.logic;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.upload.DirectoryProperties;
import sg.ncl.service.upload.data.ResumableEntity;
import sg.ncl.service.upload.data.ResumableStorage;
import sg.ncl.service.upload.domain.UploadService;
import sg.ncl.service.upload.web.ResumableInfo;

import javax.inject.Inject;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public String checkChunk(String resumableIdentifier, int resumableChunkNumber) {
        ResumableEntity entity = storage.get(resumableIdentifier);
        if (entity != null && entity.uploadedChunks.contains(new ResumableEntity.ResumableChunkNumber(resumableChunkNumber))) {
            return "Uploaded.";
        } else {
            return "Not found";
        }
    }

    @Override
    public String addChunk(ResumableInfo resumableInfo, int resumableChunkNumber, String subDirKey, String preDir) {
        String baseDir = properties.getBaseDir();
        String subDir = (subDirKey == null) ? null : properties.getSubDirs().get(subDirKey);
        if (subDir != null) {
            baseDir = baseDir + "/" + subDir;
        }
        if (preDir != null) {
            baseDir = baseDir + "/" + preDir;
        }

        //https://www.mkyong.com/java/how-to-create-directory-in-java/
        Path path = Paths.get(System.getProperty("user.home"), baseDir);
        //if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
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

        try {
            RandomAccessFile raf = new RandomAccessFile(entity.resumableFilePath, "rw");
            //Seek to position
            log.info("resumableChunkNumber: " + resumableChunkNumber + " resumableChunkSize: " + entity.resumableChunkSize);
            raf.seek((resumableChunkNumber - 1) * (long) entity.resumableChunkSize);
            //Save to file
            byte[] bytes = Base64.decodeBase64(resumableInfo.getResumableChunk());
            raf.write(bytes);
            raf.close();
        } catch (Exception e) {
            log.error("Error saving chunk: {}", e.getMessage());
            throw new BadRequestException();
        }

        //Mark as uploaded.
        entity.uploadedChunks.add(new ResumableEntity.ResumableChunkNumber(resumableChunkNumber));
        if (entity.checkIfUploadFinished()) { //Check if all chunks uploaded, and change filename
            storage.remove(entity);
            return "All finished.";
        } else {
            return "Upload";
        }
    }

}
