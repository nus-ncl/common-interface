package sg.ncl.service.version.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.version.domain.VersionInfo;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
public class VersionController {

    private final VersionInfo versionInfo;

    @Inject
    protected VersionController(final VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public VersionInfo version() {
        return versionInfo;
    }

}
