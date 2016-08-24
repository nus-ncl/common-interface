package sg.ncl.service.version.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.version.domain.Version;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
class VersionController {

    private final Version version;

    @Inject
    VersionController(final Version version) {
        this.version = version;
    }

    @GetMapping(path = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Version version() {
        return version;
    }

}
