package sg.ncl.service.registration.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.registration.RegistrationService;
import sg.ncl.service.registration.dtos.RegistrationInfo;

import javax.inject.Inject;

/**
 * @author Te Ye & Desmond
 */
@RestController
@RequestMapping(path = "/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService registrationService;

    @Inject
    protected RegistrationController(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody RegistrationInfo registrationInfo) {
        registrationService.register(registrationInfo.getCredentials(), registrationInfo.getUser(), registrationInfo.getTeam(), registrationInfo.getIsJoinTeam());
    }
}
