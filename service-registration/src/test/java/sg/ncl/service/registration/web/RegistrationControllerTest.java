package sg.ncl.service.registration.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;

import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.serializers.DateTimeDeserializer;
import sg.ncl.service.registration.serializers.DateTimeSerializer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Te Ye
 */
@TestPropertySource(properties = "flyway.enabled=false")
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
    private AdapterDeterLab adapterDeterLab;

    @Autowired
    private RestOperations restOperations;

    private MockRestServiceServer mockServer;



    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test
    public void registerOldUserJoinTeamTest() throws Exception {

        // apply to join team but since no teams exists yet
        // create stub team
        Team teamEntity = teamService.createTeam(Util.getTeamEntity());
        User user = userService.createUser(Util.getUserEntity());

        adapterDeterLab.saveDeterUserIdMapping("AAAAA", user.getId());

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
        predefinedResultJson.put("msg", "join project existing user ok");

        User owner = userService.createUser(Util.getUserEntity());
        TeamMemberInfo memberInfo = Util.getTeamMemberInfo(owner.getId(), MemberType.OWNER);
        teamService.addMember(teamEntity.getId(), memberInfo);

        mockServer.expect(requestTo(properties.getJoinProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/registrations/joinApplications").contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isAccepted());
    }

    /*
    @Test
    public void approveTeam() throws Exception {
        Team teamEntity = Util.getTeamEntity();
        UserEntity userEntity = Util.getUserEntity();

        User user = userService.createUser(userEntity);
        Team team = teamService.createTeam(teamEntity);
        TeamMemberInfo owner = Util.getTeamMemberInfo(user.getId(), MemberType.OWNER);

        userService.verifyUserEmail(user.getId(), user.getUserDetails().getEmail(), user.getVerificationKey());
        teamService.addMember(team.getId(), owner);

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "approve project OK");


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String randomToken = RandomStringUtils.randomAlphabetic(8);
        httpHeaders.set("X-Subject-Token", randomToken);
        mockServer.expect(requestTo(openStackConnectionProperties.requestTokenUrl()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess().headers(httpHeaders));

        //mockServer.expect(requestTo(openStackConnectionProperties.listUserUrl(anyString())))
        //        .andExpect(method(HttpMethod.GET))
        //        .andRespond(withStatus(HttpStatus.OK));
        when(adapterOpenStack.retrieveOpenStackUserId(anyString())).thenReturn("abc");

        //mockOpenStackServer.expect(requestTo(openStackConnectionProperties.createUserUrl()))
       //         .andExpect(method(HttpMethod.POST))
       //         .andRespond(withStatus(HttpStatus.OK));

        String randomUserId = RandomStringUtils.randomAlphabetic(8);
        JSONObject idObject = new JSONObject();
        idObject.put("id", randomUserId);
        JSONArray userArray = new JSONArray();
        userArray.put(idObject);
        JSONObject responseObject = new JSONObject();
        responseObject.put("users", userArray);
        mockOpenStackServer.expect(requestTo(openStackConnectionProperties.listUserUrl(anyString())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseObject.toString(), MediaType.APPLICATION_JSON));

        mockOpenStackServer.expect(requestTo(openStackConnectionProperties.createProjectUrl()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));


        mockServer.expect(requestTo(properties.getApproveProject())
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON));

        String deterUserIdOne = RandomStringUtils.randomAlphabetic(8);
        adapterDeterLab.saveDeterUserIdMapping(deterUserIdOne, owner.getUserId());


        mockMvc.perform(post("/registrations/teams/" + team.getId() + "/owner/" + owner.getUserId() + "?status=" + TeamStatus.APPROVED))
                .andExpect(status().isOk());
    }
*/
    @Test
    public void rejectJoinRequest() throws Exception {

        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.createTeam(one);

        User user = Util.getUserEntity();
        User user2 = Util.getUserEntity();

        User createdUser = userService.createUser(user);
        User createdUser2 = userService.createUser(user2);
        userService.addTeam(createdUser.getId(), createdTeam.getId());
        userService.addTeam(createdUser2.getId(), createdTeam.getId());

        TeamMemberInfo owner = Util.getTeamMemberInfo(createdUser.getId(), MemberType.OWNER);
        TeamMemberInfo member = Util.getTeamMemberInfo(createdUser2.getId(), MemberType.MEMBER);
        teamService.addMember(createdTeam.getId(), owner);
        teamService.addMember(createdTeam.getId(), member);

        String deterUserIdOne = RandomStringUtils.randomAlphabetic(8);
        String deterUserIdTwo = RandomStringUtils.randomAlphabetic(8);
        adapterDeterLab.saveDeterUserIdMapping(deterUserIdOne, createdUser.getId());
        adapterDeterLab.saveDeterUserIdMapping(deterUserIdTwo, createdUser2.getId());

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
        predefinedResultJson.put("msg", "process join request OK");

        mockServer.expect(requestTo(properties.getRejectJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        mockMvc.perform(delete("/registrations/teams/" + createdTeam.getId() + "/" + "members/" + member.getUserId()).contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isAccepted());

        User resultUser = userService.getUser(createdUser.getId());
        User resultUser2 = userService.getUser(createdUser2.getId());
        List<? extends TeamMember> membersList = teamService.getTeamById(createdTeam.getId()).getMembers();

        // owner should be in team
        Assert.assertThat(resultUser.getTeams().get(0), is(createdTeam.getId()));
        Assert.assertThat(membersList.size(), is(1));
        // member should be deleted from team
        Assert.assertThat(resultUser2.getTeams().isEmpty(), is(true));
    }

    @Test
    public void testGetDeterUid() throws Exception {
        final String deterUid = RandomStringUtils.randomAlphanumeric(20);
        final String nclUid = RandomStringUtils.randomAlphanumeric(20);
        adapterDeterLab.saveDeterUserIdMapping(deterUid, nclUid);

        MvcResult mvcResult = mockMvc.perform(get("/registrations/user/" + nclUid))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertThat(mvcResult.getResponse().getContentAsString(), is(deterUid));
    }
}
