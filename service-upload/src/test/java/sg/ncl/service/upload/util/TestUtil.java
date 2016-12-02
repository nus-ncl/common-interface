package sg.ncl.service.upload.util;

import sg.ncl.service.upload.web.ResumableInfo;

/**
 * Created by dcsjnh on 12/2/2016.
 */
public class TestUtil {
    public static ResumableInfo getResumableInfo() {
        return new ResumableInfo(1024, 1L, "identifier", "filename", "relativePath", "chunk");
    }
}
