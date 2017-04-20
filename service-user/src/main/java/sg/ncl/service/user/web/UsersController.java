package sg.ncl.service.user.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

import static sg.ncl.service.user.validations.Validator.isAdmin;

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

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        User one = userService.getUser(id);
        if (one == null) {
            log.warn("User not found: {}", id);
            throw new UserNotFoundException(id);
        }
        return new UserInfo(one);
    }

    // for an user to update his own personal details
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable String id, @RequestBody UserInfo user) {
        return new UserInfo(userService.updateUser(id, user));
    }

    // for admin to update user status
    @PutMapping(path = "/{id}/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserStatus(
            @AuthenticationPrincipal final Object claims,
            @PathVariable final String id,
            @PathVariable final String status) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }

        isAdmin((Claims) claims);

        return new UserInfo(userService.updateUserStatus(id, UserStatus.valueOf(status)));
    }

    @DeleteMapping(path = "/{id}")
    public String removeUser(@AuthenticationPrincipal final Object claims, @PathVariable String id) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }

        isAdmin((Claims) claims);

        return String.valueOf(userService.removeUser(id));
    }

}
