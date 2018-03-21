package sg.ncl.service.authentication.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.authentication.domain.AuthenticationService;
import sg.ncl.service.authentication.domain.Authorization;
import sg.ncl.service.authentication.exceptions.EmptyAuthorizationHeaderException;
import sg.ncl.service.authentication.exceptions.InvalidBasicAuthenticationException;
import sg.ncl.service.authentication.exceptions.UnknownAuthorizationSchemeException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import static sg.ncl.service.authentication.web.AuthenticationController.PATH;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthenticationController {

    static final String PATH = "/authentications";

    private final AuthenticationService authenticationService;

    @Inject
    AuthenticationController(@NotNull final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Authorization login(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authorization) {
        if (authorization.isEmpty()) {
            log.warn("Authorization header is empty");
            throw new EmptyAuthorizationHeaderException();
        }
        if (authorization.startsWith("Basic ")) {
            final byte[] bytes = Base64Utils.decodeFromString(authorization.substring(6));
            final String decoded = new String(bytes);
            final String[] split = decoded.split(":", 2);
            if (split.length == 2) {
                final String username = split[0];
                final String password = split[1];
                return authenticationService.login(username, password);
            }
            log.warn("Invalid basic authentication: {}", authorization);
            throw new InvalidBasicAuthenticationException(authorization);
        }
        log.warn("Unknown authorization scheme: {}", authorization);
        throw new UnknownAuthorizationSchemeException(authorization);
    }

}
