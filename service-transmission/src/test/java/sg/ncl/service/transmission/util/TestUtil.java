package sg.ncl.service.transmission.util;

import org.apache.commons.lang3.RandomStringUtils;
import sg.ncl.service.transmission.web.ResumableInfo;

/**
 * Created by dcsjnh on 12/2/2016.
 */
public class TestUtil {
    public static ResumableInfo getResumableInfo() {
        return new ResumableInfo(1, 1L, RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20), RandomStringUtils.randomAlphanumeric(20), null);
    }
}
