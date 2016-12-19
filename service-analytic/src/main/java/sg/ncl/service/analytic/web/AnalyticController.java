package sg.ncl.service.analytic.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.image.domain.Image;
import sg.ncl.service.image.domain.ImageService;
import sg.ncl.service.image.domain.ImageVisibility;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.ncl.common.validation.Validator.checkClaimsType;
import static sg.ncl.service.image.web.ImageController.PATH;

/**
 * @author Te Ye
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AnalyticController {

    static final String PATH = "/analytic";

    private final ImageService imageService;

    @Inject
    AnalyticController(@NotNull final ImageService imageService) {
        this.imageService = imageService;
    }
}
