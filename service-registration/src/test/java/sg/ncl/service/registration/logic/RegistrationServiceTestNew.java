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
import sg.ncl.service.registration.exceptions.*;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

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
        Mockito.doReturn(userWithId).when(userService).createUser(any(User.class));
        Mockito.doReturn(predefinedResultJson.toString()).when(adapterDeterLab).joinProjectNewUsers(anyString());
        Mockito.doReturn(registrationEntity).when(registrationRepository).save(any(RegistrationEntity.class));

        Registration result = registrationService.register(credentialsEntity, user, team, isJoinTeam);

        assertThat(result.getId(), is(equalTo(registrationEntity.getId())));
    }

    @Test
    public void registerTestReturnNull() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity user = Util.getUserEntity();
        UserEntity userWithId = user;
        userWithId.setId("12345678");
        Team team = Util.getTeamEntity();
        RegistrationEntity registrationEntity = Util.getRegistrationEntity();
        registrationEntity.setId(Long.parseLong("1234567890"));

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is not created");
        predefinedResultJson.put("uid", stubUid);

        Mockito.doReturn(team).when(teamService).getTeamById(team.getId());
        Mockito.doReturn(userWithId).when(userService).createUser(any(User.class));
        Mockito.doReturn(predefinedResultJson.toString()).when(adapterDeterLab).joinProjectNewUsers(anyString());
        Mockito.doReturn(registrationEntity).when(registrationRepository).save(any(RegistrationEntity.class));

        Registration result = registrationService.register(credentialsEntity, user, team, isJoinTeam);

        assertThat(result, is(nullValue()));
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

        Mockito.doReturn(teamEntity).when(teamService).createTeam(any(Team.class));
        Mockito.doReturn(userEntity).when(userService).createUser(any(User.class));
        Mockito.doReturn(predefinedResultJson.toString()).when(adapterDeterLab).applyProjectNewUsers(anyString());
        Mockito.doReturn(registrationEntity).when(registrationRepository).save(any(RegistrationEntity.class));

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

    @Test(expected = UserFormException.class)
    public void registerTestJoinTeamEmptyTeamID() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = true;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId(null);

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = UserFormException.class)
    public void registerTestJoinTeamNullTeamID() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = true;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("");

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = UserFormException.class)
    public void registerTestNotJoinTeamNullTeamName() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("id");
        teamEntity.setName(null);

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = UserFormException.class)
    public void registerTestNotJoinTeamEmptyTeamName() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("id");
        teamEntity.setName("");

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = TeamNotFoundException.class)
    public void registerTestJoinTeamOldUserUnknownTeam() throws Exception {
        String uid = RandomStringUtils.randomAlphabetic(8);
        TeamEntity teamEntity = Util.getTeamEntity();

        when(teamService.getTeamByName(anyString())).thenReturn(null);
        registrationService.registerRequestToJoinTeam(uid, teamEntity);
    }

    @Test
    public void registerTestJoinTeamOldUserKnownTeam() {
        String uid = RandomStringUtils.randomAlphabetic(8);
        TeamEntity teamEntity = Util.getTeamEntity();

        when(teamService.getTeamByName(anyString())).thenReturn(teamEntity);
        registrationService.registerRequestToJoinTeam(uid, teamEntity);

        verify(adapterDeterLab, times(1)).getDeterUserIdByNclUserId(anyString());
        verify(userService, times(1)).addTeam(anyString(), anyString());
        verify(teamService, times(1)).addMember(anyString(), any(TeamMemberInfo.class));
        verify(adapterDeterLab, times(1)).joinProject(anyString());
    }

    @Test(expected = TeamNotFoundException.class)
    public void approveJoinRequestTestNullTeam() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(null);

        registrationService.approveJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test
    public void approveJoinRequestTestUpdateUserStatus() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(createdTeam);
        when(adapterDeterLab.getDeterUserIdByNclUserId(anyString())).thenReturn(RandomStringUtils.randomAlphanumeric(20));
        when(userService.getUser(anyString())).thenReturn(createdUser);

        registrationService.approveJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);

        verify(userService, times(1)).updateUserStatus(anyString(), any(UserStatus.class));
    }

    @Test(expected = TeamNotFoundException.class)
    public void rejectJoinRequestTestNullTeam() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(null);

        registrationService.rejectJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test(expected = NoMembersInTeamException.class)
    public void rejectJoinRequestTestEmptyMemberList() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(createdTeam);

        registrationService.rejectJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test(expected = UserIsNotTeamMemberException.class)
    public void rejectJoinRequestTestUserIsNotTeamMember() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));

        teamEntity.addMember(Util.getTeamMemberInfo(RandomStringUtils.randomAlphanumeric(20), MemberType.MEMBER));

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(teamEntity);

        registrationService.rejectJoinRequest(teamEntity.getId(), userEntity.getId(), userEntity);
    }

    @Test(expected = InvalidTeamStatusException.class)
    public void approveOrRejectNewTeamTestNullStatus() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));

        registrationService.approveOrRejectNewTeam(teamEntity.getId(), userEntity.getId(), null);
    }

    @Test
    public void approveOrRejectNewTeamTestUpdateUserStatus() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        teamEntity.setStatus(TeamStatus.APPROVED);
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));
        userEntity.setStatus(UserStatus.PENDING);

        when(teamService.updateTeamStatus(anyString(), any(TeamStatus.class))).thenReturn(teamEntity);
        when(userService.getUser(anyString())).thenReturn(userEntity);

        registrationService.approveOrRejectNewTeam(teamEntity.getId(), userEntity.getId(), TeamStatus.APPROVED);

        verify(userService, times(1)).updateUserStatus(anyString(), any(UserStatus.class));
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUser() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();
        registrationService.register(credentialsEntity, null, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserLastName() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setLastName(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserJobTitle() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setJobTitle(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserEmail() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setEmail(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserPhone() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setPhone(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserInstitution() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setInstitution(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserInstitutionAbbreviation() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setInstitutionAbbreviation(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserInstitutionWeb() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setInstitutionWeb(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserAddress1() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setAddress1(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserCountry() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setCountry(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserRegion() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setRegion(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserCity() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setCity(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

    @Test(expected = UserFormException.class)
    public void userFormFieldsHasErrorsTestNullUserZipCode() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setZipCode(null);

        registrationService.register(credentialsEntity, userEntity, team, true);
    }

}
