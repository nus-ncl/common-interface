package sg.ncl.service.authentication.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.service.authentication.validation.Validator.validateForCreation;
import static sg.ncl.service.authentication.validation.Validator.validateForUpdate;
import static sg.ncl.service.authentication.web.CredentialsController.PATH;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CredentialsController {

    static final String PATH = "/credentials";

    private final CredentialsService credentialsService;

    @Inject
    CredentialsController(@NotNull final CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Credentials> getAll() {
        return credentialsService.getAll().stream().map(CredentialsInfo::new).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Credentials addCredentials(@RequestBody final CredentialsInfo credentials) {
        validateForCreation(credentials);
        return new CredentialsInfo(credentialsService.addCredentials(credentials));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Credentials updateCredentials(@PathVariable final String id, @RequestBody final CredentialsInfo credentials) {
        validateForUpdate(credentials);
        return new CredentialsInfo(credentialsService.updateCredentials(id, credentials));
    }

}
