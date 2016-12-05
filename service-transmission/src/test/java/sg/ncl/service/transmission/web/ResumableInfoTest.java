package sg.ncl.service.transmission.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import sg.ncl.service.transmission.data.ResumableEntity;

/**
 * Created by huangxian on 4/12/16.
 */
public class ResumableInfoTest {

    @Test
    public void testGetResumableInfoWithResumable() {
        final ResumableEntity entity = new ResumableEntity();
        entity.setResumableChunkSize(1);
        entity.setResumableTotalSize(1L);
        entity.setResumableIdentifier(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableFilename(RandomStringUtils.randomAlphanumeric(20));
        entity.setResumableRelativePath(RandomStringUtils.randomAlphanumeric(20));
        new ResumableInfo(entity);
    }

}
