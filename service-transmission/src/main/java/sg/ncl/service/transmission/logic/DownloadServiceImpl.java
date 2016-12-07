package sg.ncl.service.transmission.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.transmission.DirectoryProperties;
import sg.ncl.service.transmission.domain.DownloadService;
import sg.ncl.service.transmission.util.HttpUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Created by dcsjnh on 12/1/2016.
 *
 * References:
 * [1] http://www.codejava.net/java-ee/servlet/java-servlet-download-file-example
 * [2] http://stackoverflow.com/questions/55709/streaming-large-files-in-a-java-servlet
 * [3] http://stackoverflow.com/questions/685271/using-servletoutputstream-to-write-very-large-files-in-a-java-servlet-without-me
 * [4] http://stackoverflow.com/questions/32988370/download-large-file-from-server-using-rest-template-java-spring-mvc
 */
@Service
@Slf4j
public class DownloadServiceImpl implements DownloadService {

    private final DirectoryProperties properties;

    @Inject
    DownloadServiceImpl(final DirectoryProperties properties) {
        this.properties = properties;
    }

    @Override
    public void getChunks(HttpServletResponse response, String subDirKey, String preDir, String fileName) throws IOException {
        Path path = HttpUtils.getPath(properties, subDirKey, preDir);
        String filePath = path.toString() + "/" + fileName;
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);

        // modifies response
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(downloadFile.length());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inStream.close();
        outStream.close();
    }

}
