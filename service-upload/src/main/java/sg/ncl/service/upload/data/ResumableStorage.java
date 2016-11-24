package sg.ncl.service.upload.data;

/**
 * Created by dcsjnh on 11/24/2016.
 */
public interface ResumableStorage {

    public ResumableEntity get(String resumableIdentifier);

    public ResumableEntity get(int resumableChunkSize, long resumableTotalSize,
                               String resumableIdentifier, String resumableFilename,
                               String resumableRelativePath, String resumableFilePath);

    public void remove(ResumableEntity entity);

}
