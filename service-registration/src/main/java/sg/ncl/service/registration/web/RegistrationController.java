package sg.ncl.service.registration.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.team.domain.TeamStatus;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Te Ye & Desmond
 */
@Slf4j
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
    public Map<String, String> register(@RequestBody final RegistrationInfo registrationInfo) {
        Map<String, String> map = new HashMap<>();
        Registration one = registrationService.register(registrationInfo.getCredentials(), registrationInfo.getUser(), registrationInfo.getTeam(), registrationInfo.getIsJoinTeam(), registrationInfo.getNotes() );
        if (one != null) {
            map.put("id", one.getId().toString());
        }
        return map;
    }

    @PostMapping(path = "/newTeam/{nclUserId}")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerRequestToApplyTeam(@PathVariable String nclUserId, @RequestBody RegistrationInfo registrationInfo) {
        Map<String, String> map = new HashMap<>();
        Registration one = registrationService.registerRequestToApplyTeam(nclUserId, registrationInfo.getTeam());
        if (one != null) {
            map.put("id", one.getId().toString());
        }
        return map;
    }

    // old user + join team
    @PostMapping(path = "/joinApplications")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> registerRequestToJoinTeam(@RequestBody RegistrationInfo registrationInfo) {
        Map<String, String> map = new HashMap<>();
        Registration one = registrationService.registerRequestToJoinTeam(registrationInfo.getUser().getId(), registrationInfo.getTeam(), registrationInfo.getNotes());
        if (one != null) {
            map.put("id", one.getId().toString());
        }
        return map;
    }

    @PostMapping(path = "/teams/{teamId}/members/{userId}")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String approveJoinRequest(@PathVariable String teamId, @PathVariable String userId, @RequestBody RegistrationInfo registrationInfo) {
        return
                registrationService.approveJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @DeleteMapping(path = "/teams/{teamId}/members/{userId}")
    // FIXME: the path is wrong, there should not be multiple paths for different registrations; status should be OK
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String rejectJoinRequest(@PathVariable String teamId, @PathVariable String userId, @RequestBody RegistrationInfo registrationInfo) {
        return
                registrationService.rejectJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @PostMapping(path = "/teams/{teamId}/owner/{ownerId}", params = {"status"})
    // FIXME: the path is wrong, there should not be multiple paths for different registrations
    @ResponseStatus(HttpStatus.OK)
    public String approveOrRejectNewTeam(@PathVariable String teamId, @PathVariable String ownerId, @RequestParam("status") final TeamStatus teamStatus) {
        // need to specify to deterlab who is the owner so that they can set it as project_root
        // else trust level is always none
        return registrationService.approveOrRejectNewTeam(teamId, ownerId, teamStatus);
    }

    @GetMapping(path = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getDeterUid(@PathVariable String id) {
        return registrationService.getDeterUid(id);
    }
}
