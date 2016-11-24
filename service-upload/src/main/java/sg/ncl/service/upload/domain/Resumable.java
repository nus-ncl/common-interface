package sg.ncl.service.upload.domain;

/**
 * Created by dcsjnh on 11/24/2016.
 */
public interface Resumable {

    Integer getResumableChunkSize();

    Long getResumableTotalSize();

    String getResumableIdentifier();

    String getResumableFilename();

    String getResumableRelativePath();

}
