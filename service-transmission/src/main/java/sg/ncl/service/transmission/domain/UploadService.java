package sg.ncl.service.transmission.domain;

import sg.ncl.service.transmission.web.ResumableInfo;

import java.io.IOException;

/**
 * Created by dcsjnh on 11/24/2016.
 */
public interface UploadService {

    boolean deleteUpload(String subDirKey, String preDir, String fileName) throws IOException;

    UploadStatus checkChunk(String resumableIdentifier, int resumableChunkNumber);

    UploadStatus addChunk(ResumableInfo resumableInfo, int resumableChunkNumber, String subDirKey, String preDir);

}
