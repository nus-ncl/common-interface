package sg.ncl.service.registration;

import org.springframework.stereotype.Service;
import sg.ncl.service.authentication.logic.AuthenticationService;
import sg.ncl.service.user.services.UserService;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@Service
public class RegistrationService {

    private final AuthenticationService authenticationService;
    //    private final TeamService teamService;
    private final UserService userService;

    @Inject
    protected RegistrationService(final AuthenticationService authenticationService, /*final TeamService teamService,*/ final UserService userService) {
        this.authenticationService = authenticationService;
//        this.teamService=teamService;
        this.userService = userService;
    }

    public void register() {

    }

}
