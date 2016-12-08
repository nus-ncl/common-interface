package sg.ncl.service.user.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = UsersController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UsersController {

    static final String PATH = "/users";

    private final UserService userService;

    @Inject
    UsersController(@NotNull final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        User one = userService.getUser(id);
        if (one == null) {
            log.warn("User not found: {}", id);
            throw new UserNotFoundException(id);
        }
        return new UserInfo(one);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable String id, @RequestBody UserInfo user) {
        userService.updateUser(id, user);
    }

    @PutMapping(path = "/{id}/emails/{emailBase64}")
    public UserStatus verifyEmail(@PathVariable String id, @PathVariable String emailBase64, @RequestBody VerificationKeyInfo keyInfo) {
        final String email = new String(Base64.decodeBase64(emailBase64));
        return userService.verifyEmail(id, email, keyInfo.getKey());
    }
}
