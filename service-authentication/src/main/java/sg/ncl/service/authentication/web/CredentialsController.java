package sg.ncl.service.authentication.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.service.authentication.validation.Validator.*;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Credentials> getAll() {
        return credentialsService.getAll().stream().map(CredentialsInfo::new).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Credentials addCredentials(@RequestBody final CredentialsInfo credentials) {
        addCheck(credentials);
        return new CredentialsInfo(credentialsService.addCredentials(credentials));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Credentials updateCredentials(@PathVariable final String id, @RequestBody final CredentialsInfo credentials, @AuthenticationPrincipal Object claims) {
        checkClaimsType(claims);
        updateCheck(credentials, (Claims) claims);
        return new CredentialsInfo(credentialsService.updateCredentials(id, credentials, (Claims) claims));
    }

}
