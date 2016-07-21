package sg.ncl.service.registration.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.team.domain.TeamStatus;

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
    public void register(@RequestBody final RegistrationInfo registrationInfo) {
//        System.out.println("User: " + registrationInfo.getUser());
//        System.out.println("Team: " + registrationInfo.getTeam());
//        System.out.println("Registration: " + registrationInfo.getRegistration());
        registrationService.register(registrationInfo.getCredentials(), registrationInfo.getUser(), registrationInfo.getTeam(), registrationInfo.getIsJoinTeam());
    }

    @RequestMapping(path = "/newTeam/{nclUserId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRequestToApplyTeam(@PathVariable String nclUserId, @RequestBody RegistrationInfo registrationInfo) {
        registrationService.registerRequestToApplyTeam(nclUserId, registrationInfo.getTeam());
    }

    // old user + join team
    @RequestMapping(path = "/joinApplications", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void registerRequestToJoinTeam(@RequestBody RegistrationInfo registrationInfo) {
        registrationService.registerRequestToJoinTeam(registrationInfo.getUser().getId(), registrationInfo.getTeam());
    }

    @RequestMapping(path = "/teams/{teamId}/members/{userId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void approveJoinRequest(@PathVariable String teamId, @PathVariable String userId, @RequestBody RegistrationInfo registrationInfo) {
        registrationService.approveJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @RequestMapping(path = "/teams/{teamId}", method = RequestMethod.POST, params = {"status"})
    @ResponseStatus(HttpStatus.OK)
    public void approveTeam(@PathVariable String teamId, @RequestParam("status") final TeamStatus teamStatus) {
        registrationService.approveTeam(teamId, teamStatus);
    }
}
