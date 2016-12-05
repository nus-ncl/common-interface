package sg.ncl.service.upload.domain;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dcsjnh on 12/1/2016.
 */
@FunctionalInterface
public interface DownloadService {

    void getChunks(HttpServletResponse response, String subDirKey, String preDir, String fileName) throws IOException;

}
