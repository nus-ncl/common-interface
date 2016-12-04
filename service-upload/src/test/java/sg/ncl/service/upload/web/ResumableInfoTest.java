package sg.ncl.service.upload.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.upload.data.ResumableEntity;

/**
 * Created by huangxian on 4/12/16.
 */
public class ResumableInfoTest {

    @Test
    public void testGetResumableInfoWithResumable() {
        final ResumableEntity entity = new ResumableEntity();
        entity.resumableChunkSize = 1;
        entity.resumableTotalSize = 1L;
        entity.resumableIdentifier = RandomStringUtils.randomAlphanumeric(20);
        entity.resumableFilename = RandomStringUtils.randomAlphanumeric(20);
        entity.resumableRelativePath = RandomStringUtils.randomAlphanumeric(20);
        new ResumableInfo(entity);
    }

}
