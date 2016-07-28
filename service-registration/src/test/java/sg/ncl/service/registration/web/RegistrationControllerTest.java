package sg.ncl.service.registration.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.serializers.DateTimeDeserializer;
import sg.ncl.service.registration.serializers.DateTimeSerializer;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Te Ye
 */
public class RegistrationControllerTest extends AbstractTest {

    private MockMvc mockMvc;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Inject
    private ConnectionProperties properties;

    @Inject
    private TeamService teamService;

    @Inject
    private UserService userService;

    @Inject
    private AdapterDeterlab adapterDeterlab;

    @Autowired
    private RestOperations restOperations;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test
    public void registerNewUserJoinExistingTeamTest() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity userEntity = Util.getUserEntity();

        // apply to join team but since no teams exists yet
        // create stub team
        Team team = teamService.addTeam(Util.getTeamEntity());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String credentialsJSON = gson.toJson(credentialsEntity);
        String userJSON = gson.toJson(userEntity);
        String teamJSON = gson.toJson(team);

        JSONObject mainJSON = new JSONObject();
        JSONObject credentialsFields = new JSONObject(credentialsJSON);
        JSONObject userFields = new JSONObject(userJSON);
        JSONObject teamFields = new JSONObject(teamJSON);

        mainJSON.put("credentials", credentialsFields);
        mainJSON.put("user", userFields);
        mainJSON.put("team", teamFields);
        mainJSON.put("isJoinTeam", true);

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getJoinProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/registrations").contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void registerNewUserApplyNewTeamTest() throws Exception {
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity userEntity = Util.getUserEntity();

        TeamEntity teamEntity = Util.getTeamEntity();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String credentialsJSON = gson.toJson(credentialsEntity);
        String userJSON = gson.toJson(userEntity);
        String teamJSON = gson.toJson(teamEntity);

        JSONObject mainJSON = new JSONObject();
        JSONObject credentialsFields = new JSONObject(credentialsJSON);
        JSONObject userFields = new JSONObject(userJSON);
        JSONObject teamFields = new JSONObject(teamJSON);

        mainJSON.put("credentials", credentialsFields);
        mainJSON.put("user", userFields);
        mainJSON.put("team", teamFields);
        mainJSON.put("isJoinTeam", false);

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getApplyProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/registrations").contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void registerOldUserJoinTeamTest() throws Exception {

        // apply to join team but since no teams exists yet
        // create stub team
        Team teamEntity = teamService.addTeam(Util.getTeamEntity());
        User user = userService.createUser(Util.getUserEntity());

        adapterDeterlab.saveDeterUserIdMapping("AAAAA", user.getId());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String teamJSON = gson.toJson(teamEntity);
        String userJSON = gson.toJson(user);

        JSONObject mainJSON = new JSONObject();
        JSONObject teamFields = new JSONObject(teamJSON);
        JSONObject userFields = new JSONObject(userJSON);

        mainJSON.put("user", userFields);
        mainJSON.put("team", teamFields);

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user has logged in and joined a project");

        mockServer.expect(requestTo(properties.getJoinProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/registrations/joinApplications").contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void addTeamUserControllerTest() throws Exception {
        // UserController addTeamMember test can only be tested here because it requires an existing team in the database
        User user = userService.createUser(Util.getUserEntity());
        Team team = teamService.addTeam(Util.getTeamEntity());

        // craft the RequestBody to add user to team
        JSONObject userObject = new JSONObject();
        JSONArray teamArray = new JSONArray();
        teamArray.put(team.getId());
        userObject.put("teams", teamArray);

        mockMvc.perform(post("/users/" + user.getId() + "/teams").contentType(MediaType.APPLICATION_JSON).content(userObject.toString()))
                .andExpect(status().isOk());

        // after add user complete, retrieve the user from database
        // assert that the team ids are identical
        MvcResult result = mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONObject resultObject = new JSONObject(result.getResponse().getContentAsString());
        JSONArray resulTeamArray = resultObject.getJSONArray("teams");
        Assert.assertThat(team.getId(), is(resulTeamArray.get(0).toString()));
    }

    @Test
    public void approveTeam() throws Exception {
        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(one);
        TeamMemberInfo owner = Util.getTeamMemberInfo(TeamMemberType.OWNER);
        teamService.addTeamMember(createdTeam.getId(), owner);

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "project approved");

        mockServer.expect(requestTo(properties.getApproveProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/registrations/teams/" + createdTeam.getId() + "?status=" + TeamStatus.APPROVED))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectJoinRequest() throws Exception {

        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(one);

        User user = Util.getUserEntity();
        User user2 = Util.getUserEntity();

        User createdUser = userService.createUser(user);
        User createdUser2 = userService.createUser(user2);
        userService.addTeam(createdUser.getId(), createdTeam.getId());
        userService.addTeam(createdUser2.getId(), createdTeam.getId());

        TeamMemberInfo owner = Util.getTeamMemberInfo(createdUser.getId(), TeamMemberType.OWNER);
        TeamMemberInfo member = Util.getTeamMemberInfo(createdUser2.getId(), TeamMemberType.MEMBER);
        teamService.addTeamMember(createdTeam.getId(), owner);
        teamService.addTeamMember(createdTeam.getId(), member);

        // craft the RequestBody to remove user from team
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String userJSON = gson.toJson(createdUser);

        JSONObject object = new JSONObject();
        JSONObject userFields = new JSONObject(userJSON);
        object.put("user", userFields);

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join request rejected");

        mockServer.expect(requestTo(properties.getRejectJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(delete("/registrations/teams/" + createdTeam.getId() + "/" + "members/" + member.getUserId()).contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isAccepted());

        User resultUser = userService.findUser(createdUser.getId());
        User resultUser2 = userService.findUser(createdUser2.getId());
        List<? extends TeamMember> membersList = teamService.getTeamById(createdTeam.getId()).getMembers();

        // owner should be in team
        Assert.assertThat(resultUser.getTeams().get(0), is(createdTeam.getId()));
        Assert.assertThat(membersList.get(0).getUserId(), is(createdUser.getId()));
        // member should be deleted from team
        Assert.assertThat(resultUser2.getTeams().isEmpty(), is(true));
    }
}
