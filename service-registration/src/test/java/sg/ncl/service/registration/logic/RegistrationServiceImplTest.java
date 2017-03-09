package sg.ncl.service.registration.logic;

import freemarker.template.Template;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.data.jpa.RegistrationEntity;
import sg.ncl.service.registration.data.jpa.RegistrationRepository;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.exceptions.*;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.exceptions.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @Autors : Vu
 */

public class RegistrationServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    @Mock
    private DomainProperties domainProperties;
    @Mock
    private Template emailValidationTemplate;
    @Mock
    private Template applyTeamRequestTemplate;
    @Mock
    private Template replyTeamRequestTemplate;
    @Mock
    private Template applyJoinTeamRequestTemplate;
    @Mock
    private Template replyJoinTeamRequestTemplate;

    private RegistrationService registrationService;
    private boolean isJoinTeam = true;

    @Before
    public void before() {
        assertThat(mockingDetails(credentialsService).isMock()).isTrue();
        assertThat(mockingDetails(teamService).isMock()).isTrue();
        assertThat(mockingDetails(userService).isMock()).isTrue();
        assertThat(mockingDetails(registrationRepository).isMock()).isTrue();
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(mailService).isMock()).isTrue();

        registrationService = new RegistrationServiceImpl(
                credentialsService, teamService, userService, registrationRepository, adapterDeterLab, mailService, domainProperties,
                emailValidationTemplate, applyTeamRequestTemplate, replyTeamRequestTemplate, applyJoinTeamRequestTemplate, replyJoinTeamRequestTemplate);
    }

    @Test
    public void testRegisterRequestToApplyTeamEmptyTeamName() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setName("");

        UserEntity userEntity = Util.getUserEntity();
        when(userService.getUser(anyString())).thenReturn(userEntity);

        exception.expect(TeamNameNullOrEmptyException.class);
        registrationService.registerRequestToApplyTeam(RandomStringUtils.randomAlphanumeric(8), teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToApplyTeamEmptyUserId() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity teamEntity = Util.getTeamEntity();

        exception.expect(UserIdNullOrEmptyException.class);
        registrationService.registerRequestToApplyTeam("", teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToApplyTeamNoUser() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity teamEntity = Util.getTeamEntity();

        when(userService.getUser(anyString())).thenReturn(null);

        exception.expect(UserNotFoundException.class);
        registrationService.registerRequestToApplyTeam(RandomStringUtils.randomAlphanumeric(8), teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToApplyTeamDuplicateTeam() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();

        userEntity.setId(RandomStringUtils.randomAlphanumeric(8));

        when(userService.getUser(anyString())).thenReturn(userEntity);
        when(teamService.getTeamByName(anyString())).thenReturn(teamEntity);

        exception.expect(TeamNameAlreadyExistsException.class);
        registrationService.registerRequestToApplyTeam(userEntity.getId(), teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToApplyTeam() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();

        userEntity.setId(RandomStringUtils.randomAlphanumeric(8));

        when(userService.getUser(anyString())).thenReturn(userEntity);
        when(teamService.getTeamByName(anyString())).thenReturn(null);
        when(teamService.createTeam(any(Team.class))).thenReturn(teamEntity);

        registrationService.registerRequestToApplyTeam(userEntity.getId(), teamEntity, randomNotes);

        verify(teamService, times(1)).createTeam(any(Team.class));
        verify(userService, times(1)).addTeam(anyString(), anyString());
        verify(teamService, times(1)).addMember(anyString(), any(TeamMember.class));
    }

    // to test mapping from ncl team id to deter project id
    @Test
    public void testRegisterRequestToApplyTeamNullTeamIdMapping() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        UserEntity userEntity = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId(null);

        userEntity.setId(RandomStringUtils.randomAlphanumeric(8));

        when(userService.getUser(anyString())).thenReturn(userEntity);
        when(teamService.createTeam(any(Team.class))).thenReturn(teamEntity);

        exception.expect(TeamIdNullOrEmptyException.class);

        registrationService.registerRequestToApplyTeam(userEntity.getId(), teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToJoinTeamEmptyTeamName() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setName("");

        UserEntity userEntity = Util.getUserEntity();
        when(userService.getUser(anyString())).thenReturn(userEntity);

        exception.expect(TeamNameNullOrEmptyException.class);
        registrationService.registerRequestToJoinTeam(RandomStringUtils.randomAlphanumeric(8), teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToJoinTeamEmptyUserId() {
        TeamEntity teamEntity = Util.getTeamEntity();
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);

        exception.expect(UserIdNullOrEmptyException.class);
        registrationService.registerRequestToJoinTeam("", teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToJoinTeamNoTeam() {
        String uid = RandomStringUtils.randomAlphabetic(8);
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);

        TeamEntity teamEntity = Util.getTeamEntity();
        when(teamService.getTeamByName(anyString())).thenReturn(null);

        UserEntity userEntity = Util.getUserEntity();
        when(userService.getUser(anyString())).thenReturn(userEntity);

        exception.expect(TeamNotFoundException.class);
        registrationService.registerRequestToJoinTeam(uid, teamEntity, randomNotes);
    }

    @Test
    public void testRegisterRequestToJoinTeam() {
        String uid = RandomStringUtils.randomAlphabetic(8);
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity teamEntity = Util.getTeamEntity();

        when(teamService.getTeamByName(anyString())).thenReturn(teamEntity);
        UserEntity userEntity = Util.getUserEntity();
        when(userService.getUser(anyString())).thenReturn(userEntity);
        registrationService.registerRequestToJoinTeam(uid, teamEntity, randomNotes);

        verify(adapterDeterLab, times(1)).getDeterUserIdByNclUserId(anyString());
        verify(userService, times(1)).addTeam(anyString(), anyString());
        verify(teamService, times(1)).addMember(anyString(), any(TeamMemberInfo.class));
        verify(adapterDeterLab, times(1)).joinProject(anyString());
    }

    @Test
    public void testRegisterEmptyPassword() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity userEntity = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();

        credentialsEntity.setPassword("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, teamEntity, isJoinTeam, randomNotes);
    }

    @Test
    public void testRegisterJoinTeamEmptyTeamID() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = true;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam, randomNotes);
    }

    @Test
    public void testRegisterNotJoinTeamEmptyTeamName() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("id");
        teamEntity.setName("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam, randomNotes);
    }

    @Test
    public void testRegisterApplyDuplicateTeamName() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId("id");
        String teamName = teamEntity.getName();

        when(teamService.getTeamByName(teamName)).thenReturn(teamEntity);

        exception.expect(TeamNameAlreadyExistsException.class);
        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam, randomNotes);
    }

    @Test
    public void testRegister() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
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
        Mockito.doReturn(user).when(userService).getUser(anyString());

        Registration result = registrationService.register(credentialsEntity, user, team, isJoinTeam, randomNotes);

        assertThat(result.getId()).isEqualTo(registrationEntity.getId());
    }

    @Test
    public void testRegisterReturnNull() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
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

        Registration result = registrationService.register(credentialsEntity, user, team, isJoinTeam, randomNotes);

        assertThat(result).isNull();
    }

    @Test
    public void testRegisterApplyNewProject() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
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

        Registration result = registrationService.register(credentialsEntity, userEntity, teamEntity, isJoinTeam, randomNotes);
        assertThat(result.getId()).isEqualTo(registrationEntity.getId());
    }

    // to test mapping from ncl team id to deter project id
    @Test
    public void testRegisterNulLTeamIdMapping() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity userEntity = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setId(null);
        isJoinTeam = false;

        Mockito.doReturn(teamEntity).when(teamService).createTeam(any(Team.class));

        exception.expect(TeamIdNullOrEmptyException.class);

        registrationService.register(credentialsEntity, userEntity, teamEntity, isJoinTeam, randomNotes);
    }

    @Test
    public void testApproveJoinRequestNotTeamOwner() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(false);

        exception.expect(UserIsNotTeamOwnerException.class);
        registrationService.approveJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test
    public void testApproveJoinRequestEmptyTeam() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(null);

        exception.expect(TeamNotFoundException.class);
        registrationService.approveJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test
    public void testApproveJoinRequestUpdateUserStatus() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(createdTeam);
        when(adapterDeterLab.getDeterUserIdByNclUserId(anyString())).thenReturn(RandomStringUtils.randomAlphanumeric(20));
        when(userService.getUser(anyString())).thenReturn(createdUser);

        registrationService.approveJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);

        verify(userService, times(1)).updateUserStatus(anyString(), any(UserStatus.class));
    }

    @Test
    public void testRejectJoinRequestNotTeamOwner() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(false);

        exception.expect(UserIsNotTeamOwnerException.class);
        registrationService.rejectJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test
    public void testRejectJoinRequestEmptyTeam() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(null);

        exception.expect(TeamNotFoundException.class);
        registrationService.rejectJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test
    public void testRejectJoinRequestEmptyMemberList() {
        Team createdTeam = Util.getTeamEntity();
        User createdUser = Util.getUserEntity();

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(createdTeam);

        exception.expect(NoMembersInTeamException.class);
        registrationService.rejectJoinRequest(createdTeam.getId(), createdUser.getId(), createdUser);
    }

    @Test
    public void testRejectJoinRequestUserIsTeamMember() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));
        teamEntity.addMember(Util.getTeamMemberInfo(userEntity.getId(), MemberType.MEMBER));

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(teamEntity);
        when(userService.getUser(anyString())).thenReturn(userEntity);

        registrationService.rejectJoinRequest(teamEntity.getId(), userEntity.getId(), userEntity);

        verify(adapterDeterLab, times(1)).processJoinRequest(anyString());
    }

    @Test
    public void testRejectJoinRequestUserIsNotTeamMember() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));
        teamEntity.addMember(Util.getTeamMemberInfo(RandomStringUtils.randomAlphanumeric(20), MemberType.MEMBER));

        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(teamEntity);

        exception.expect(UserIsNotTeamMemberException.class);
        registrationService.rejectJoinRequest(teamEntity.getId(), userEntity.getId(), userEntity);
    }

    @Test
    public void testApproveOrRejectNewTeamEmptyTeamId() {
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));

        exception.expect(TeamIdNullOrEmptyException.class);
        registrationService.approveOrRejectNewTeam("", userEntity.getId(), null);
    }

    @Test
    public void testApproveOrRejectNewTeamEmptyOwnerId() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        exception.expect(UserIdNullOrEmptyException.class);
        registrationService.approveOrRejectNewTeam(teamEntity.getId(), userEntity.getId(), null);
    }

    @Test
    public void testApproveOrRejectNewTeamEmptyStatus() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));

        exception.expect(InvalidTeamStatusException.class);
        registrationService.approveOrRejectNewTeam(teamEntity.getId(), userEntity.getId(), TeamStatus.PENDING);
    }

    @Test
    public void testApproveOrRejectNewTeamApprovedTeamStatusPendingUserStatus() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        teamEntity.setStatus(TeamStatus.APPROVED);
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));
        userEntity.setStatus(UserStatus.PENDING);

        when(teamService.updateTeamStatus(anyString(), any(TeamStatus.class))).thenReturn(teamEntity);
        when(userService.getUser(anyString())).thenReturn(userEntity);

        registrationService.approveOrRejectNewTeam(teamEntity.getId(), userEntity.getId(), TeamStatus.APPROVED);

        verify(userService, times(1)).updateUserStatus(anyString(), any(UserStatus.class));
        verify(teamService, times(1)).updateMemberStatus(anyString(), anyString(), any(MemberStatus.class));
        verify(adapterDeterLab, times(1)).approveProject(anyString());
    }

    @Test
    public void testApproveOrRejectNewTeamRejectedTeam() {
        TeamEntity teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));
        teamEntity.addMember(Util.getTeamMemberInfo(RandomStringUtils.randomAlphanumeric(20), MemberType.MEMBER));

        teamEntity.setStatus(TeamStatus.APPROVED);
        userEntity.setId(RandomStringUtils.randomAlphanumeric(20));
        userEntity.setStatus(UserStatus.PENDING);

        when(teamService.updateTeamStatus(anyString(), any(TeamStatus.class))).thenReturn(teamEntity);
        when(userService.getUser(anyString())).thenReturn(userEntity);
        when(teamService.getTeamById(anyString())).thenReturn(teamEntity);

        registrationService.approveOrRejectNewTeam(teamEntity.getId(), userEntity.getId(), TeamStatus.REJECTED);

        verify(teamService, times(1)).getTeamById(anyString());
        verify(userService, times(1)).removeTeam(anyString(), anyString());
        verify(teamService, times(1)).removeTeam(anyString());
        verify(adapterDeterLab, times(1)).rejectProject(anyString());
    }

    @Test
    public void testGetDeterUid() {
        final String nclUid = RandomStringUtils.randomAlphanumeric(20);
        registrationService.getDeterUid(nclUid);
        verify(adapterDeterLab, times(1)).getDeterUserIdByNclUserId(nclUid);
    }

    @Test
    public void testUserFormFieldsHasErrorsNullUser() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();
        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, null, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserFirstName() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setFirstName("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserLastName() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setLastName("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserJobTitle() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setJobTitle("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserEmail() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setEmail("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserPhone() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setPhone("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserInstitution() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setInstitution("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserInstitutionAbbreviation() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setInstitutionAbbreviation("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserInstitutionWeb() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().setInstitutionWeb("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserAddress1() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setAddress1("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserCountry() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setCountry("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserRegion() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setRegion("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserCity() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setCity("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

    @Test
    public void testUserFormFieldsHasErrorsEmptyUserZipCode() {
        String randomNotes = RandomStringUtils.randomAlphanumeric(20);
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        Team team = Util.getTeamEntity();

        UserEntity userEntity = Util.getUserEntity();
        userEntity.getUserDetails().getAddress().setZipCode("");

        exception.expect(IncompleteRegistrationFormException.class);
        registrationService.register(credentialsEntity, userEntity, team, true, randomNotes);
    }

}
