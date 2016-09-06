package sg.ncl.service.user.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.TeamsNullOrEmptyException;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@RestController
@Slf4j
public class UsersController {

    private final UserService userService;

    @Inject
    UsersController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        User one = userService.getUser(id);
        if(one == null) {
            log.warn("User not found: {}", id);
            throw new UserNotFoundException(id);
        }
        return new UserInfo(one);
    }

    @PutMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable String id, @RequestBody UserInfo user) {
        userService.updateUser(id, user);
    }

    // FIXME: this method is no long used; to be removed
    @RequestMapping(path = "/users/{id}/teams", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addTeam(@PathVariable String id, @RequestBody UserInfo user) {
        if (user.getTeams() == null || user.getTeams().isEmpty()) {
            log.warn("Teams field is null or empty: {}", user.getTeams());
            throw new TeamsNullOrEmptyException();
        }
        // keep it simple for RegistrationService when parsing add user to team
        userService.addTeam(id, user.getTeams().get(0));
    }

    @PutMapping(path = "/users/{id}/emails/{emailBase64}")
    public UserStatus verifyEmail(@PathVariable String id, @PathVariable String emailBase64, @RequestBody VerificationKeyInfo keyInfo) {
        final String email = new String(Base64.decodeBase64(emailBase64));
        return userService.verifyEmail(id, email, keyInfo.getKey());
    }
}
