package sg.ncl.service.registration.logic;

import freemarker.template.Template;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.common.DomainProperties;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.data.jpa.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.TeamNameDuplicateException;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ActiveProfiles("mock-registration-service")
public class RegistrationServiceTestNew extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    DeterLabUserRepository deterLabUserRepository;
    @Mock
    ConnectionProperties connectionProperties;
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
    private RegistrationService registrationService;
    @Autowired
    private DomainProperties domainProperties;
    @Autowired
    private Template freemarkerConfiguration;
    private boolean isJoinTeam = true;

    @Before
    public void setUp() throws Exception {
        registrationService = new RegistrationServiceImpl(credentialsService,
                teamService, userService, registrationRepository, adapterDeterLab, mailService,
                domainProperties, freemarkerConfiguration);
    }

    @Test
    public void registerTest() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity user = Util.getUserEntity();
        UserEntity userWithId = user;
        userWithId.setId("12345678");
        Team team = Util.getTeamEntity();
        RegistrationEntity registrationEntity = Util.getRegistrationEntity();
        registrationEntity.setId(Long.parseLong("1234567890"));

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        Mockito.doReturn(team).when(teamService).getTeamById(team.getId());
        Mockito.doReturn(userWithId).when(userService).createUser(Mockito.any(User.class));
        Mockito.doReturn(predefinedResultJson.toString()).when(adapterDeterLab).joinProjectNewUsers(Mockito.anyString());
        Mockito.doReturn(registrationEntity).when(registrationRepository).save(Mockito.any(RegistrationEntity.class));

        Registration result = registrationService.register(credentialsEntity, user, team, isJoinTeam);

        assertThat(result.getId(), is(equalTo(registrationEntity.getId())));
    }

    @Test
    public void registerTestApplyNewProject() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setId("12345678");
        TeamEntity teamEntity = Util.getTeamEntity();
        RegistrationEntity registrationEntity = Util.getRegistrationEntity();
        registrationEntity.setId(Long.parseLong("1234567890"));
        isJoinTeam = false;

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        Mockito.doReturn(teamEntity).when(teamService).createTeam(Mockito.any(Team.class));
        Mockito.doReturn(userEntity).when(userService).createUser(Mockito.any(User.class));
        Mockito.doReturn(predefinedResultJson.toString()).when(adapterDeterLab).applyProjectNewUsers(Mockito.anyString());
        Mockito.doReturn(registrationEntity).when(registrationRepository).save(Mockito.any(RegistrationEntity.class));

        Registration result = registrationService.register(credentialsEntity, userEntity, teamEntity, isJoinTeam);
        assertThat(result.getId(), is(equalTo(registrationEntity.getId())));
    }

    @Test(expected = TeamNameDuplicateException.class)
    public void registerTestApplyDuplicateTeamName() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("id");
        String teamName = teamEntity.getName();

        Mockito.doReturn(teamEntity).when(teamService).getTeamByName(teamName);

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

}
