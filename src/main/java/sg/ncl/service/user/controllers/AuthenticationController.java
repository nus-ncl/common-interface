package sg.ncl.service.user.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.user.data.jpa.repositories.UserCredentialsRepository;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {

    private final UserCredentialsRepository userCredentialsRepository;

    @Inject
    protected AuthenticationController(final UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void login() {
        return;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logout() {
        return;
    }

}
