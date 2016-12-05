package sg.ncl.service.image.web;

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
public class ImageController {

    static final String PATH = "/images";

    private final ImageService imageService;

    @Inject
    ImageController(@NotNull final ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Image> getAll(@RequestParam(value = "teamId", required = false) String teamId, @RequestParam(value = "visibility", required = false) ImageVisibility imageVisibility) {
        return imageService.getAll(teamId, imageVisibility).stream().map(ImageInfo::new).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Image getImage(@PathVariable final Long id) {
        return new ImageInfo(imageService.getImage(id));
    }

    /**
     * Invokes the adapter to retrieve the list of saved images
     */
    @GetMapping(path = "/teams/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getSavedImages(@PathVariable final String id) {
        return imageService.getSavedImages(id);
    }

    /**
     * Creates a saved image
     * status is ACCEPTED because creating an image is not an instant process
     * @param image
     * @return the created image id
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Long addImage(@RequestBody final ImageInfo image,  @AuthenticationPrincipal Object claims) {
        checkClaimsType(claims);
        return new ImageInfo(imageService.addImage(image, (Claims) claims)).getId();
    }
}
