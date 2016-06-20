package sg.ncl.service.registration.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.data.jpa.entities.RegistrationEntity;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.serializers.DateTimeDeserializer;
import sg.ncl.service.team.serializers.DateTimeSerializer;
import sg.ncl.service.user.data.jpa.entities.UserEntity;

import javax.inject.Inject;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Te Ye on 16-Jun-16.
 */
public class RegistrationControllerTest extends AbstractTest {

    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Ignore
    public void registerNewUserTest() throws Exception {
        // TODO TBD
        // password
        // UserEntity
        // TeamEntity
        CredentialsEntity credentialsEntity = Util.getCredentialsEntity();
        UserEntity userEntity = Util.getUserEntity();
        TeamEntity teamEntity = Util.getTeamEntity();
        RegistrationEntity registrationEntity = Util.getRegistrationEntity();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String credentialsJSON = gson.toJson(credentialsEntity);
        String userJSON = gson.toJson(userEntity);
        String teamJSON = gson.toJson(teamEntity);
        String registrationJSON = gson.toJson(registrationEntity);

        JSONObject mainJSON = new JSONObject();
        JSONObject credentialsFields = new JSONObject(credentialsJSON);
        JSONObject userFields = new JSONObject(userJSON);
        JSONObject teamFields = new JSONObject(teamJSON);

        mainJSON.put("credentials", credentialsFields);
        mainJSON.put("user", userFields);
        mainJSON.put("team", teamFields);

        mockMvc.perform(post("/registrations").contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isOk());
    }
}
