package sg.ncl.service.upload.domain;

import sg.ncl.service.upload.web.ResumableInfo;

/**
 * Created by dcsjnh on 11/24/2016.
 */
public interface UploadService {

    String checkChunk(String resumableIdentifier, int resumableChunkNumber);

    String addChunk(ResumableInfo resumableInfo, int resumableChunkNumber, String subDirKey);

}
