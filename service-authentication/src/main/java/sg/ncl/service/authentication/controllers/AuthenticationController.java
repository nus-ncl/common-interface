package sg.ncl.service.authentication.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.authentication.AuthenticationService;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Inject
    protected AuthenticationController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void login() {

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logout() {

    }

}
