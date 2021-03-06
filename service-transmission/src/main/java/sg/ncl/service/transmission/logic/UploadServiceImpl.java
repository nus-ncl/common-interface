package sg.ncl.service.transmission.logic;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.transmission.DirectoryProperties;
import sg.ncl.service.transmission.data.ResumableEntity;
import sg.ncl.service.transmission.data.ResumableStorage;
import sg.ncl.service.transmission.domain.UploadService;
import sg.ncl.service.transmission.domain.UploadStatus;
import sg.ncl.service.transmission.exceptions.UploadAlreadyExistsException;
import sg.ncl.service.transmission.exceptions.UploadOutOfBaseDirException;
import sg.ncl.service.transmission.util.HttpUtils;
import sg.ncl.service.transmission.web.ResumableInfo;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by dcsjnh on 11/24/2016.
 *
 * References:
 * [1] http://alvinalexander.com/java/java-file-exists-directory-exists
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
    public void deleteDirectory(String subDirKey, String preDir) {
        Path path = HttpUtils.getPath(properties, subDirKey, preDir);
        HttpUtils.removeDirectory(new File(path.toString()));
    }

    @Override
    public boolean deleteUpload(String subDirKey, String preDir, String fileName) throws IOException {

        Path path = HttpUtils.getPath(properties, subDirKey, preDir);
        String filePath = path.toString() + "/" + fileName;

        boolean isFilePathSafe = HttpUtils.isFilePathSafe(properties , fileName);

        if(isFilePathSafe)
        {
            File file = new File(filePath);
            return Files.deleteIfExists(file.toPath());
        }
        else
        {
            return false;
        }
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

        boolean isFilePathSafe = HttpUtils.isFilePathSafe(properties , resumableInfo.getResumableFilename());

        if(isFilePathSafe){

            Path path = HttpUtils.getPath(properties, subDirKey, preDir);
            createDirectoryOrCheckFileExists(path, resumableInfo);

            //Here we add a ".temp" to every transmission file to indicate NON-FINISHED
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
        else{
            return UploadStatus.NOT_FOUND;
        }

    }
    
    private void createDirectoryOrCheckFileExists(Path path, ResumableInfo resumableInfo) {
        boolean isFilePathSafe = HttpUtils.isFilePathSafe(properties , resumableInfo.getResumableFilename());

        if(isFilePathSafe) {
            //if directory exists?
            if (!path.toFile().exists()) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    log.error("Unable to create directory path: {}", e);
                    throw new BadRequestException();
                }
            } else {
                File file = new File(path.toString() + "/" + resumableInfo.getResumableFilename());
                if (file.exists()) {
                    throw new UploadAlreadyExistsException("Upload file already exist in directory.");
                }
            }
        }
        else
        {
            throw new UploadOutOfBaseDirException("Arbitrary File Write via Path Traversal.");
        }
    }

    private void writeChunk(ResumableEntity entity, int resumableChunkNumber, ResumableInfo resumableInfo) {

        boolean isFilePathSafe = HttpUtils.isFilePathSafe(properties , resumableInfo.getResumableFilename());

        if(isFilePathSafe) {
            try (RandomAccessFile raf = new RandomAccessFile(entity.getResumableFilePath(), "rw")) {
                //Seek to position
                log.info("resumableChunkNumber: " + resumableChunkNumber + " resumableChunkSize: " + entity.getResumableChunkSize());
                raf.seek((resumableChunkNumber - 1) * (long) entity.getResumableChunkSize());
                //Save to file
                byte[] bytes = Base64.decodeBase64(resumableInfo.getResumableChunk());
                raf.write(bytes);
            } catch (Exception e) {
                log.error("Error saving chunk: {}", e);
                throw new BadRequestException();
            }
        }
        else
        {
            throw new UploadOutOfBaseDirException("Arbitrary File Write via Path Traversal.");
        }
    }

}
