package sg.ncl.service.registration.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.registration.RegistrationService;
import sg.ncl.service.registration.dtos.RegistrationData;
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

    // new user + join team
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody RegistrationInfo registrationInfo) {
//        System.out.println("User: " + registrationInfo.getUser());
//        System.out.println("Team: " + registrationInfo.getTeam());
//        System.out.println("Registration: " + registrationInfo.getRegistration());
        registrationService.register(registrationInfo.getCredentials(), registrationInfo.getUser(), registrationInfo.getTeam(), registrationInfo.getIsJoinTeam());
    }

    // old user + join team
    @RequestMapping(path = "/joinApplications", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void registerRequestToJoinTeam(@RequestBody RegistrationInfo registrationInfo) {
        registrationService.registerRequestToJoinTeam(registrationInfo.getUser().getId(), registrationInfo.getTeam());
    }
}
