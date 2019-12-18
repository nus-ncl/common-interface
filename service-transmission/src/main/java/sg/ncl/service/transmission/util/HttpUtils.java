package sg.ncl.service.transmission.util;

import lombok.extern.slf4j.Slf4j;
import sg.ncl.service.transmission.DirectoryProperties;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dcsjnh on 11/24/2016.
 *
 * References:
 * [1] https://github.com/23/resumable.js/blob/master/samples/java/src/main/java/resumable/js/upload/HttpUtils.java
 * [2] http://www.codejava.net/java-se/file-io/clean-and-remove-a-non-empty-directory
 * [3] https://docs.oracle.com/javase/tutorial/essential/io/pathOps.html
 */
@Slf4j
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

        Path path = Paths.get(System.getProperty("user.home"));
        return Paths.get(path.getRoot().toString(), baseDir);
    }

    public static boolean isFilePathUnsafe(DirectoryProperties properties, String filename){
        boolean unsafePath = false;
        String baseDir = properties.getBaseDir();

        if(filename.contains("../") || filename.contains("./") || filename.contains("..")){
            unsafePath = true;
        }

        Path baseDirPath = Paths.get(baseDir);
        Path userPath = Paths.get(filename);
        final Path resolvedPath = baseDirPath.resolve(userPath).normalize();
        if (!resolvedPath.startsWith(baseDirPath)) {
            unsafePath = true;
        }

        return unsafePath;
    }


    public static void removeDirectory(File dir) {
        boolean deleted;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            deleted = dir.delete();
        } else {
            deleted = dir.delete();
        }

        log.info("Deleted file {}: {}", dir, deleted);
    }

}
