package sg.ncl.service.user.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.user.UserService;
import sg.ncl.service.user.domain.User;

import javax.inject.Inject;
import javax.validation.Valid;

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

    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addUser(@RequestBody @Valid User user) {
//        final UserEntity userEntity = new UserEntity();
//        final UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
//        userDetailsEntity.setFirstName("");
//        userDetailsEntity.setLastName("");
//        userEntity.setUserDetails(userDetailsEntity);
//        new UserCredentialsEntity();
//        userService.save(userEntity);
        return;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable String id) {
//        final UserEntity user = userService.findOne(id);
        return null;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable String id) {

    }

}
