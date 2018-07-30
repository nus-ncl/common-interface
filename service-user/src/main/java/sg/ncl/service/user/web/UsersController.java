package sg.ncl.service.user.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

import static sg.ncl.common.validation.Validator.checkClaimsType;
import static sg.ncl.common.validation.Validator.checkAdmin;

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
        checkClaimsType(claims);
        checkAdmin((Claims) claims);
        return new UserInfo(userService.updateUserStatus(id, UserStatus.valueOf(status)));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String removeUser(@AuthenticationPrincipal final Object claims, @PathVariable String id) {
        checkClaimsType(claims);
        checkAdmin((Claims) claims);
        return String.valueOf(userService.removeUser(id));
    }

    @GetMapping(path = "/{id}/publicKeys")
    @ResponseStatus(HttpStatus.OK)
    public String getPublicKeys(@AuthenticationPrincipal final Object claims, @PathVariable String id) {
        checkClaimsType(claims);
        return userService.getPublicKeys(((Claims) claims).getSubject());
    }

    @PostMapping(path = "/{id}/publicKeys", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String addPublicKey(@AuthenticationPrincipal final Object claims, @PathVariable String id, @RequestBody PublicKeyInfo info) {
        checkClaimsType(claims);
        return userService.addPublicKey(info.getPublicKey(), info.getPassword(), ((Claims) claims).getSubject());
    }

    @DeleteMapping(path = "/{userid}/publicKeys/{keyid}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePublicKey(@AuthenticationPrincipal final Object claims, @PathVariable String userid, @PathVariable String keyid) {
        checkClaimsType(claims);
        return userService.deletePublicKey(keyid, ((Claims) claims).getSubject());
    }

    /**
     * For a new student member to update his info including first name, last name, phone and password
     * when the student first time logs in
     *
     * @param id uuid of the user
     * @param studentInfo including first name, last name, phone, password and the key
     * @return the updated user
     */
    @PutMapping(path = "/{id}/studentInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User changePasswordStudent(@PathVariable String id, @RequestBody StudentInfo studentInfo) {
        final String firstName = studentInfo.getFirstName();
        final String lastName = studentInfo.getLastName();
        final String phone = studentInfo.getPhone();
        final String password = studentInfo.getPassword();
        final String key = studentInfo.getKey();

        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() ||
                phone == null || phone.isEmpty() || password == null || password.isEmpty() ||
                key == null || key.isEmpty()) {
            log.warn("Error access /users/{}/studentInfo: Bad Request", id);
            throw new BadRequestException(studentInfo.toString());
        }
        return new UserInfo(userService.changePasswordStudent(id, firstName, lastName, phone, password, key));
    }
}
