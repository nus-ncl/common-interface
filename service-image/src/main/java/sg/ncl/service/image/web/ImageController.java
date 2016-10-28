package sg.ncl.service.image.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.image.domain.ImageService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import java.util.Map;

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
    public Map<String, String> getAll() {
        return null;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getImage(@PathVariable final String id) {
        return null;
    }

    // status is ACCEPTED because creating an image is not an instant process
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> addImage() {
        return null;
    }
}
