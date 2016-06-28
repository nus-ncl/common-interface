package sg.ncl.service.user.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserDetails;
import sg.ncl.service.user.logic.UserService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UserService userService;

    @Inject
    protected UsersController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<User> get() {
        return userService.get();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        return new UserInfo(userService.find(id));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable String id, @RequestBody UserDetailsInfo user) {
        userService.update(id, user);
    }

    @RequestMapping(path = "/seed", method = RequestMethod.POST)
    public void seedData() {
        userService.seedData();
    }

    @RequestMapping(path = "/addUserToTeam/{id}/{teamId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addUserToTeam(@PathVariable String id, @PathVariable String teamId) {
        userService.addUserToTeam(id, teamId);
    }

}
