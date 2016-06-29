package sg.ncl.service.registration.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.team.logic.TeamService;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.serializers.DateTimeDeserializer;
import sg.ncl.service.team.serializers.DateTimeSerializer;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.adapter.deterlab.ConnectionProperties;

import javax.inject.Inject;

import java.time.ZonedDateTime;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        TeamEntity teamEntity = teamService.createTeam(Util.getTeamEntity());

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
        mainJSON.put("isJoinTeam", true);

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getAddUsersUri()))
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
}
