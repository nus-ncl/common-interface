package sg.ncl.service.authentication.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.authentication.exceptions.EmptyAuthorizationHeaderException;
import sg.ncl.service.authentication.exceptions.InvalidBasicAuthenticationException;
import sg.ncl.service.authentication.exceptions.UnknownAuthorizationSchemeException;
import sg.ncl.service.authentication.logic.AuthenticationService;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @Inject
    protected AuthenticationController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authorization) {
        if (authorization.isEmpty()) {
            logger.warn("Authorization header is empty");
            throw new EmptyAuthorizationHeaderException();
        }
        if (authorization.startsWith("Basic ")) {
            final byte[] bytes = Base64Utils.decodeFromString(authorization.substring(6));
            final String decoded = new String(bytes);
            final String[] split = decoded.split(":");
            if (split.length == 2) {
                final String username = split[0];
                final String password = split[1];
                return authenticationService.login(username, password);
            }
            logger.warn("Invalid basic authentication: {}", authorization);
            throw new InvalidBasicAuthenticationException(authorization);
        }
        logger.warn("Unknown authorization scheme: {}", authorization);
        throw new UnknownAuthorizationSchemeException(authorization);
    }

}
