package sg.ncl.service.registration.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.user.web.VerificationKeyInfo;

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

    // new user + join team / create team
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> register(@RequestBody final RegistrationInfo registrationInfo) {
        Map<String, String> map = new HashMap<>();
        Registration one = registrationService.register(registrationInfo.getCredentials(), registrationInfo.getUser(), registrationInfo.getTeam(), registrationInfo.getIsJoinTeam());
        if (one != null) {
            map.put("id", one.getId().toString());
        }
        return map;
    }

    @PostMapping(path = "/newTeam/{nclUserId}")
    // FIX ME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerRequestToApplyTeam(@PathVariable String nclUserId,
                                                          @RequestBody RegistrationInfo registrationInfo) {
        Map<String, String> map = new HashMap<>();
        Registration one = registrationService.registerRequestToApplyTeam(nclUserId, registrationInfo.getTeam());
        if (one != null) {
            map.put("id", one.getId().toString());
        }
        return map;
    }

    // old user + join team
    @PostMapping(path = "/joinApplications")
    // FIX ME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> registerRequestToJoinTeam(@RequestBody RegistrationInfo registrationInfo) {
        Map<String, String> map = new HashMap<>();
        Registration one = registrationService.registerRequestToJoinTeam(registrationInfo.getUser().getId(), registrationInfo.getTeam());
        if (one != null) {
            map.put("id", one.getId().toString());
        }
        return map;
    }

    @PostMapping(path = "/teams/{teamId}/members/{userId}")
    // FIX ME: the path is wrong, there should not be multiple paths for different registrations; status should be ACCEPTED
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String approveJoinRequest(@PathVariable String teamId, @PathVariable String userId,
                                     @RequestBody RegistrationInfo registrationInfo) {
        return
                registrationService.approveJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @DeleteMapping(path = "/teams/{teamId}/members/{userId}")
    // FIX ME: the path is wrong, there should not be multiple paths for different registrations; status should be OK
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String rejectJoinRequest(@PathVariable String teamId, @PathVariable String userId,
                                    @RequestBody RegistrationInfo registrationInfo) {
        return
                registrationService.rejectJoinRequest(teamId, userId, registrationInfo.getUser());
    }

    @PostMapping(path = "/teams/{teamId}/owner/{ownerId}", params = {"status"})
    // FIX ME: the path is wrong, there should not be multiple paths for different registrations
    @ResponseStatus(HttpStatus.OK)
    public String approveOrRejectNewTeam(@PathVariable String teamId, @PathVariable String ownerId, @RequestParam("status") final TeamStatus teamStatus, @RequestBody(required=false) String reason) {
        // need to specify to deterlab who is the owner so that they can set it as project_root
        // else trust level is always none
        return registrationService.approveOrRejectNewTeam(teamId, ownerId, teamStatus, reason);
    }

    @GetMapping(path = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getDeterUid(@PathVariable String id) {
        return registrationService.getDeterUid(id);
    }

    @PutMapping(path = "/users/{id}/emails/{emailBase64}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean verifyEmail(@PathVariable String id, @PathVariable String emailBase64,
                               @RequestBody VerificationKeyInfo keyInfo) {
        final String email = new String(Base64.decodeBase64(emailBase64));
        return registrationService.verifyEmail(id, email, keyInfo.getKey());
    }

    /**
     * register a number of student members to the specified team
     * this function only works for class project
     * need to be invoked by the project/team leader
     *
     * @param id team id, e.g., efa8bd9c-07da-4e31-8586-540f63c0fc81
     * @param emails a String in the format "hello+111-25@gmail.com\r\nhello-111.25@gmail.com\r\nhello.111_25@gmail.com"
     * @param claims
     * @return
     */
    @PostMapping(path ="/teams/{id}/students")
    @ResponseStatus(HttpStatus.OK)
    public void addStudentsByEmail(@PathVariable final String id,
                                     @RequestBody final String emails,
                                     @AuthenticationPrincipal final Object claims) {
        if (!(claims instanceof Claims)) {
            // throw forbidden
            log.warn("Invalid authentication principal: {}", claims);
            throw new ForbiddenException();
        }

        String leaderId = ((Claims) claims).getSubject();
        if (leaderId == null || leaderId.isEmpty()) {
            log.warn("Team leader ID is not supplied");
            throw new BadRequestException("Team leader ID is not supplied");
        }

        registrationService.addStudentsByEmail(id, leaderId, emails);
    }
}
