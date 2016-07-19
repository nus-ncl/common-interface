package sg.ncl.service.registration.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.exceptions.*;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

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
public class RegistrationServiceTest extends AbstractTest {

    @Inject
    private UserService userService;

    @Inject
    private TeamService teamService;

    @Inject
    private ConnectionProperties properties;

    @Inject
    private RegistrationService registrationService;

    @Inject
    private AdapterDeterlab adapterDeterlab;

    @Autowired
    private RestOperations restOperations;

    private MockRestServiceServer mockServer;

    private boolean isJoinTeam = true;

    @Before
    public void setUp() throws Exception {
        mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test
    public void registerTest() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();

        // apply to join team but since no teams exists yet
        // create stub team
        Team team = teamService.addTeam(Util.getTeamEntity());

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getJoinProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        registrationService.register(credentialsEntity, user, team, isJoinTeam);
    }

    @Test
    public void registerTestApplyNewProject() {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();

        // apply to join team but since no teams exists yet
        // create stub team
        TeamEntity teamEntity = Util.getTeamEntity();
        isJoinTeam = false;
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getApplyProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = RegisterTeamNameDuplicateException.class)
    public void registerTestApplyDuplicateTeamName() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();
        isJoinTeam = false;

        TeamEntity teamEntity = Util.getTeamEntity();
        String teamName = teamEntity.getName();
        teamService.addTeam(teamEntity);

        // purposely register for an already saved team
        // don't have to mock server since will throw exception
        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = UserFormException.class)
    public void registerTestUserFieldsError() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getInvalidUserEntity();

        // apply to join team but since no teams exists yet
        // create stub team
        Team team = teamService.addTeam(Util.getTeamEntity());

        // don't have to mock server since will throw exception
        registrationService.register(credentialsEntity, user, team, isJoinTeam);
    }

    @Test(expected = UserFormException.class)
    public void registerTestCredentialsError() throws Exception {
        CredentialsEntity credentialsEntity = Util.getInvalidCredentialsEntity();
        User user = Util.getUserEntity();

        // apply to join team but since no teams exists yet
        // create stub team
        Team team = teamService.addTeam(Util.getTeamEntity());

        // don't have to mock server since will throw exception
        registrationService.register(credentialsEntity, user, team, isJoinTeam);
    }

    @Test(expected = UserFormException.class)
    public void registerTestNoSuchTeamError() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        User user = Util.getUserEntity();

        TeamEntity teamEntity = Util.getInvalidTeamEntity();

        // don't have to mock server since will throw exception
        registrationService.register(credentialsEntity, user, teamEntity, isJoinTeam);
    }

    @Test(expected = RegisterTeamNameEmptyException.class)
    public void registerJoinTeamOldUserEmptyTeam() throws Exception {
        String uid = RandomStringUtils.randomAlphabetic(8);
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setName(null);
        // don't have to mock server since will throw exception
        registrationService.registerRequestToJoinTeam(uid, teamEntity);
    }

    @Test(expected = RegisterUidNullException.class)
    public void registerJoinTeamOldUserEmptyUser() throws Exception {
        String uid = null;
        TeamEntity teamEntity = Util.getTeamEntity();
        // don't have to mock server since will throw exception
        registrationService.registerRequestToJoinTeam(uid, teamEntity);
    }

    @Test
    public void registerApproveJoinRequestGood() throws Exception {
        Team team = Util.getTeamEntity();
        User owner = Util.getUserEntity();
        User member = Util.getUserEntity();
        User savedOwner = userService.createUser(owner);

        String teamId = teamService.addTeam(team).getId();
        String ownerId = savedOwner.getId();
        String memberId = userService.createUser(member).getId();

        // need to create entry in the Deterlab User Repository
        adapterDeterlab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(8), ownerId);
        adapterDeterlab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(8), memberId);

        // add owner and members to team members repository

        // set owner to approved
        TeamMemberEntity member1 = new TeamMemberEntity();
        member1.setUserId(ownerId);
        member1.setJoinedDate(ZonedDateTime.now());
        member1.setMemberType(TeamMemberType.OWNER);
        member1.setMemberStatus(TeamMemberStatus.APPROVED);

        TeamMemberEntity member2 = new TeamMemberEntity();
        member2.setUserId(memberId);
        member2.setJoinedDate(ZonedDateTime.now());
        member2.setMemberType(TeamMemberType.MEMBER);
        member2.setMemberStatus(TeamMemberStatus.PENDING);

        teamService.addTeamMember(teamId, new TeamMemberInfo(member1));
        teamService.addTeamMember(teamId, new TeamMemberInfo(member2));

        /*==== start mock the adapter deterlab call ===*/
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join request approved");

        mockServer.expect(requestTo(properties.getApproveJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        registrationService.approveJoinRequest(teamId, memberId, savedOwner);

        /*==== end mock the adapter deterlab call ===*/

        Team resultTeam = teamService.getTeamById(teamId);
        List<? extends TeamMember> teamMembersList = resultTeam.getMembers();

        // should have the owner and newly approved member
        Assert.assertThat(teamMembersList.size(), is(2));

        for (TeamMember teamMember : teamMembersList) {
            if (!teamMember.getMemberStatus().equals(TeamMemberStatus.APPROVED)) {
                Assert.fail("One of the team member " + teamMember.getUserId() + " is still PENDING");
            }
            if (!teamMember.getUserId().equals(ownerId) && !teamMember.getUserId().equals(memberId)) {
                Assert.fail("No such user id in the team " + teamMember.getUserId());
            }
        }
    }

    @Test(expected = UserIsNotTeamOwnerException.class)
    public void registerApproveJoinRequestBad() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        User userEntity = Util.getUserEntity();
        String teamId = teamService.addTeam(teamEntity).getId();
        registrationService.approveJoinRequest(teamId, userEntity.getId(), userEntity);
    }

}
