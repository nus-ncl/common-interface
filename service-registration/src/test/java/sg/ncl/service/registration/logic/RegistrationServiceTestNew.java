package sg.ncl.service.registration.logic;

import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.common.DomainProperties;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.RegisterTeamNameDuplicateException;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

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

    @Mock
    private DomainProperties domainProperties;
    @Mock
    private Configuration freemarkerConfiguration;

    private boolean isJoinTeam = true;

    @Before
    public void setUp() throws Exception {
       registrationService = new RegistrationServiceImpl(credentialsService,
               teamService, userService, registrationRepository, adapterDeterLab, mailService,
               domainProperties, freemarkerConfiguration);
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

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

}
