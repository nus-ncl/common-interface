package sg.ncl.service.upload.logic;

import org.springframework.stereotype.Component;
import sg.ncl.service.upload.data.ResumableEntity;
import sg.ncl.service.upload.data.ResumableStorage;

import java.util.HashMap;

/**
 * Created by dcsjnh on 11/24/2016.
 *
 * References:
 * [1] https://github.com/23/resumable.js/blob/master/samples/java/src/main/java/resumable/js/upload/ResumableInfoStorage.java
 */
@Component
public class ResumableStorageImpl implements ResumableStorage {

    private HashMap<String, ResumableEntity> mMap = new HashMap<>();

    /**
     * Just get a ResumableEntity.
     *
     * @param resumableIdentifier
     * @return
     */
    @Override
    public ResumableEntity get(String resumableIdentifier) {
        return mMap.get(resumableIdentifier);
    }

    /**
     * Get ResumableEntity from mMap or Create a new one.
     *
     * @param resumableChunkSize
     * @param resumableTotalSize
     * @param resumableIdentifier
     * @param resumableFilename
     * @param resumableRelativePath
     * @param resumableFilePath
     * @return
     */
    @Override
    public ResumableEntity get(int resumableChunkSize, long resumableTotalSize,
                               String resumableIdentifier, String resumableFilename,
                               String resumableRelativePath, String resumableFilePath) {

        ResumableEntity entity = mMap.get(resumableIdentifier);

        if (entity == null) {
            entity = new ResumableEntity();

            entity.resumableChunkSize     = resumableChunkSize;
            entity.resumableTotalSize     = resumableTotalSize;
            entity.resumableIdentifier    = resumableIdentifier;
            entity.resumableFilename      = resumableFilename;
            entity.resumableRelativePath  = resumableRelativePath;
            entity.resumableFilePath      = resumableFilePath;

            mMap.put(resumableIdentifier, entity);
        }

        return entity;
    }

    /**
     * Remove ResumableEntity.
     *
     * @param entity
     */
    @Override
    public void remove(ResumableEntity entity) {
        mMap.remove(entity.resumableIdentifier);
    }

}
