package sg.ncl.service.transmission.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.transmission.DirectoryProperties;
import sg.ncl.service.transmission.domain.DownloadService;
import sg.ncl.service.transmission.util.HttpUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Created by dcsjnh on 12/1/2016.
 *
 * References:
 * [1] http://stackoverflow.com/questions/15800565/spring-mvc-large-files-for-download-outofmemoryexception
 */
@Controller
@RequestMapping(path = DownloadController.PATH)
@Slf4j
public class DownloadController {

    static final String PATH = "/downloads";

    private final DownloadService downloadService;
    private final DirectoryProperties properties;

    @Inject
    DownloadController(@NotNull final DownloadService downloadService, final DirectoryProperties properties) {
        this.downloadService = downloadService;
        this.properties = properties;
    }


    @GetMapping(params = {"filename"})
    public void getFile(@AuthenticationPrincipal Object claims, @RequestParam("filename") String filename, HttpServletResponse response) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }

        try {
            String decodeFileName = UriUtils.decode(filename, "UTF-8");

            if (HttpUtils.isFilePathSafe(properties, decodeFileName)) {
                downloadService.getChunks(response, null, null, UriUtils.decode(filename, "UTF-8"));
            }
            else
            {
                throw new UnauthorizedException("Unauthorized");
            }

        } catch (IOException e) {
            log.error("Unable to download file: {}", e);
            throw new NotFoundException();
        }
    }

}
