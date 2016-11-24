package sg.ncl.service.upload.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.upload.domain.UploadService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by dcsjnh on 11/24/2016.
 *
 * References:
 * [1] https://github.com/23/resumable.js/blob/master/samples/java/src/main/java/resumable/js/upload/UploadServlet.java
 */
@RestController
@RequestMapping(path = UploadController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UploadController {

    static final String PATH = "/uploads";

    private final UploadService uploadService;

    @Inject
    UploadController(@NotNull final UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping(value = "/chunks/{resumableChunkNumber}/files/{resumableIdentifier}")
    @ResponseStatus(HttpStatus.OK)
    public String checkUpload(@AuthenticationPrincipal Object claims,
                              @PathVariable String resumableIdentifier,
                              @PathVariable String resumableChunkNumber) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return uploadService.checkChunk(resumableIdentifier, Integer.parseInt(resumableChunkNumber));
    }

    @PostMapping(value = "/chunks/{resumableChunkNumber}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String fileUpload(@AuthenticationPrincipal Object claims,
                             @RequestBody @Valid ResumableInfo resumableInfo,
                             @PathVariable String resumableChunkNumber) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        return uploadService.addChunk(resumableInfo, Integer.parseInt(resumableChunkNumber), null);
    }

}
