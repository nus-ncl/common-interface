package sg.ncl.service.transmission.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.transmission.domain.UploadService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

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

    @GetMapping(params = {"filename"})
    public String deleteUpload(@AuthenticationPrincipal Object claims, @RequestParam("filename") String filename) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        try {
            if (uploadService.deleteUpload("", "", filename)) {
                log.info("File {} deleted.", filename);
                return "Deleted";
            } else {
                log.info("File {} not deleted.", filename);
                return "Not Deleted";
            }
        } catch (IOException e) {
            log.error("Unable to delete file: {}", e);
            throw new BadRequestException();
        }
    }

    @GetMapping(value = "/chunks/{resumableChunkNumber}/files/{resumableIdentifier}")
    @ResponseStatus(HttpStatus.OK)
    public String checkUpload(@AuthenticationPrincipal Object claims,
                              @PathVariable String resumableIdentifier,
                              @PathVariable String resumableChunkNumber) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        switch (uploadService.checkChunk(resumableIdentifier, Integer.parseInt(resumableChunkNumber))) {
            case UPLOADED:
                return "Uploaded";
            case NOT_FOUND:
                return "Not Found";
            default:
                return "";
        }
    }

    @PostMapping(value = "/chunks/{resumableChunkNumber}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String fileUpload(@AuthenticationPrincipal Object claims,
                             @RequestBody @Valid ResumableInfo resumableInfo,
                             @PathVariable String resumableChunkNumber) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        switch (uploadService.addChunk(resumableInfo, Integer.parseInt(resumableChunkNumber), null, null)) {
            case FINISHED:
                return "Finished";
            case UPLOAD:
                return "Upload";
            default:
                return "";
        }
    }

}
