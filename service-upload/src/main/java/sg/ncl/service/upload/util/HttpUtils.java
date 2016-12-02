package sg.ncl.service.upload.util;

import sg.ncl.service.upload.DirectoryProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dcsjnh on 11/24/2016.
 *
 * References:
 * [1] https://github.com/23/resumable.js/blob/master/samples/java/src/main/java/resumable/js/upload/HttpUtils.java
 */
public class HttpUtils {

    private HttpUtils() {}

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }
    /**
     * Convert String to long
     * @param value
     * @param def default value
     * @return
     */
    public static long toLong(String value, long def) {
        if (isEmpty(value)) {
            return def;
        }

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return def;
        }
    }

    /**
     * Convert String to int
     * @param value
     * @param def default value
     * @return
     */
    public static int toInt(String value, int def) {
        if (isEmpty(value)) {
            return def;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return def;
        }
    }

    public static Path getPath(DirectoryProperties properties, String subDirKey, String preDir) {
        String baseDir = properties.getBaseDir();
        String subDir = (subDirKey == null) ? null : properties.getSubDirs().get(subDirKey);
        if (subDir != null) {
            baseDir = baseDir + "/" + subDir;
        }
        if (preDir != null) {
            baseDir = baseDir + "/" + preDir;
        }

        return Paths.get(System.getProperty("user.home"), baseDir);
    }

}
