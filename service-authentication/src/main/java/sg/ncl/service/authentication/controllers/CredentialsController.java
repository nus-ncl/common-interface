package sg.ncl.service.authentication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.common.exceptions.BadRequestException;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.dtos.CredentialsInfo;
import sg.ncl.service.authentication.services.CredentialsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE)
public class CredentialsController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsController.class);

    private final CredentialsService credentialsService;

    @Inject
    protected CredentialsController(final CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<? extends Credentials> getAll() {
        final List<? extends Credentials> all = credentialsService.getAll();
        final List<CredentialsInfo> collect = all.stream().map(o -> new CredentialsInfo(o.getUsername(), null, o.getUserId(), o.getStatus())).collect(Collectors.toCollection(ArrayList::new));
        return collect;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addCredentials(final CredentialsInfo credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null || credentials.getUserId() == null) {
            throw new BadRequestException();
        }
        credentialsService.addCredentials(credentials);
    }

    @RequestMapping(path = "")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updatePassword(final CredentialsInfo credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new BadRequestException();
        }
        credentialsService.updatePassword(credentials);
    }

}
