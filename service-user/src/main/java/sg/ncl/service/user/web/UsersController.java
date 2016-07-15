package sg.ncl.service.user.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.exceptions.TeamsNullOrEmptyException;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;

    @Inject
    protected UsersController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<User> get() {
        return userService.getAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        return new UserInfo(userService.findUser(id));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable String id, @RequestBody UserInfo user) {
        userService.updateUser(id, user);
    }

    @RequestMapping(path = "/{id}/teams", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addTeam(@PathVariable String id, @RequestBody UserInfo user) {
        if (user.getTeams() == null || user.getTeams().isEmpty()) {
            logger.warn("Teams field is null or empty: {}", user.getTeams());
            throw new TeamsNullOrEmptyException();
        }
        // keep it simple for RegistrationService when parsing add user to team
        userService.addTeam(id, user.getTeams().get(0));
    }

}
