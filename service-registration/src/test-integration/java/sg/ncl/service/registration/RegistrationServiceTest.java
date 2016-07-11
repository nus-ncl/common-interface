package sg.ncl.service.registration;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.exceptions.RegisterTeamNameDuplicateException;
import sg.ncl.service.registration.exceptions.RegisterTeamNameEmptyException;
import sg.ncl.service.registration.exceptions.RegisterUidNullException;
import sg.ncl.service.registration.exceptions.UserFormException;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.logic.UserService;

import javax.inject.Inject;

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

}
