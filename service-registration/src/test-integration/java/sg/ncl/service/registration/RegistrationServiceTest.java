package sg.ncl.service.registration;

import org.junit.Ignore;
import org.junit.Test;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.logic.AuthenticationService;
import sg.ncl.service.authentication.logic.CredentialsService;
import sg.ncl.service.registration.data.jpa.repositories.RegistrationRepository;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.services.UserService;
import sg.ncl.service.registration.Util;

import javax.inject.Inject;

/**
 * Created by Te Ye on 13-Jun-16.
 */
public class RegistrationServiceTest extends AbstractTest {

    @Inject
    private CredentialsService credentialsService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private TeamService teamService;

    @Inject
    private UserService userService;

    @Inject
    private RegistrationRepository registrationRepository;

    @Test
    @Ignore
    public void registerTest() {
        // Need the adapter service to be up on BOSS first
        RegistrationService registrationService = new RegistrationService(authenticationService, credentialsService, teamService, userService, registrationRepository);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();

        registrationService.register(credentialsEntity, user, teamEntity);
    }

}
