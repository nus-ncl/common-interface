package sg.ncl.service.registration.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.team.domain.TeamStatus;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author Te Ye & Desmond
 */
@RestController
@RequestMapping(path = "/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService registrationService;

    @Inject
    RegistrationController(@NotNull final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // new user + join team
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody final RegistrationInfo registrationInfo) {
        registrationService.register(registrationInfo.getCredentials(), registrationInfo.getUser(), registrationInfo.getTeam(), registrationInfo.getIsJoinTeam());
    }

    @PostMapping(path = "/newTeam/{nclUserId}")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRequestToApplyTeam(@PathVariable String nclUserId, @RequestBody RegistrationInfo registrationInfo) {
        registrationService.registerRequestToApplyTeam(nclUserId, registrationInfo.getTeam());
    }

    // old user + join team
    @PostMapping(path = "/joinApplications")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String registerRequestToJoinTeam(@RequestBody RegistrationInfo registrationInfo) {
        return registrationService.registerRequestToJoinTeam(registrationInfo.getUser().getId(), registrationInfo.getTeam());
    }

    @PostMapping(path = "/teams/{teamId}/members/{userId}")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void approveJoinRequest(@PathVariable String teamId, @PathVariable String userId, @RequestBody RegistrationInfo registrationInfo) {
        registrationService.approveJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @DeleteMapping(path = "/teams/{teamId}/members/{userId}")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be OK
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void rejectJoinRequest(@PathVariable String teamId, @PathVariable String userId, @RequestBody RegistrationInfo registrationInfo) {
        registrationService.rejectJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @PostMapping(path = "/teams/{teamId}/owner/{ownerId}", params = {"status"})
    // FIXME: the path is wrong, there should not be multiple paths for different registrations
    @ResponseStatus(HttpStatus.OK)
    public void approveTeam(@PathVariable String teamId, @PathVariable String ownerId, @RequestParam("status") final TeamStatus teamStatus) {
        // need to specify to deterlab who is the owner so that they can set it as project_root
        // else trust level is always none
        registrationService.approveTeam(teamId, ownerId, teamStatus);
    }

    @GetMapping(path = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getDeterUid(@PathVariable String id) {
        return registrationService.getDeterUid(id);
    }

    @PutMapping(path = "/activation", params = {"uid", "key"})
    @ResponseStatus(HttpStatus.OK)
    private void activateAccount(@RequestParam("uid") final String uid, @RequestParam("key") final String key) {
        registrationService.activateAccount(uid, key);
    }
}
