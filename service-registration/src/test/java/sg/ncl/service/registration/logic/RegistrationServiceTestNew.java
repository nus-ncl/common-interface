package sg.ncl.service.registration.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.RegisterTeamIdEmptyException;
import sg.ncl.service.registration.exceptions.RegisterTeamNameDuplicateException;
import sg.ncl.service.registration.exceptions.RegisterTeamNameEmptyException;
import sg.ncl.service.registration.exceptions.RegisterUidNullException;
import sg.ncl.service.registration.exceptions.UserFormException;
import sg.ncl.service.registration.exceptions.UserIsNotTeamOwnerException;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.exceptions.NoOwnerInTeamException;
import sg.ncl.service.team.exceptions.TeamIdNullOrEmptyException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Te Ye
 */
public class RegistrationServiceTestNew extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private UserService userService;

    @Mock
    private TeamService teamService;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private AdapterDeterLab adapterDeterLab;

    @Mock
    private MailService mailService;

    @Mock
    DeterLabUserRepository deterLabUserRepository;

    @Mock
    ConnectionProperties connectionProperties;

    private RegistrationService registrationService;




    //private RestOperations restOperations;

    //private MockRestServiceServer mockServer;

    private boolean isJoinTeam = true;

    @Before
    public void setUp() throws Exception {
       // registrationService = new RegistrationServiceImpl(credentialsService,
       //         teamService, userService, registrationRepository, adapterDeterLab, mailService);
        registrationService = new RegistrationServiceImpl(credentialsService,
                teamService, userService, registrationRepository, deterLabUserRepository, connectionProperties, mailService);
        //mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test(expected = RegisterTeamNameDuplicateException.class)
    public void registerTestApplyDuplicateTeamName() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("id");
        String teamName = teamEntity.getName();

        Mockito.doReturn(teamEntity).when(teamService).getTeamByName(teamName);

        // purposely register for an already saved team
        // don't have to mock server since will throw exception
        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

}
