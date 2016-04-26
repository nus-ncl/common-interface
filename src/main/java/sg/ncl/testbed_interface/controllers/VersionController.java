package sg.ncl.testbed_interface.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.testbed_interface.dtos.VersionInfo;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/version")
public class VersionController {

    private final VersionInfo versionInfo;

    @Inject
    protected VersionController(final VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public VersionInfo version() {
        return versionInfo;
    }

}
