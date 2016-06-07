package sg.ncl.service.authentication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.dtos.CredentialsInfo;
import sg.ncl.service.authentication.exceptions.NullPasswordException;
import sg.ncl.service.authentication.exceptions.NullUserIdException;
import sg.ncl.service.authentication.exceptions.NullUsernameException;
import sg.ncl.service.authentication.services.CredentialsService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.service.authentication.controllers.CredentialsController.PATH;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class CredentialsController {

    public static final String PATH = "/credentials";

    private static final Logger logger = LoggerFactory.getLogger(CredentialsController.class);

    private final CredentialsService credentialsService;

    @Inject
    protected CredentialsController(final CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<? extends Credentials> getAll() {
        return credentialsService.getAll().stream().map(CredentialsInfo::new).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addCredentials(@RequestBody final CredentialsInfo credentials) {
        if (credentials.getUsername() == null) {
            throw new NullUsernameException();
        }
        if (credentials.getPassword() == null) {
            throw new NullPasswordException();
        }
        if (credentials.getId() == null) {
            throw new NullUserIdException();
        }
        credentialsService.addCredentials(credentials);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCredentials(@PathVariable final String id, @RequestBody final CredentialsInfo credentials) {
        credentialsService.updateCredentials(id, credentials);
    }

}
