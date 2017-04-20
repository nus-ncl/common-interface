package sg.ncl.service.registration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.exception.ExceptionProperties;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.AbstractTest;
import sg.ncl.service.registration.Util;
import sg.ncl.service.registration.data.jpa.RegistrationEntity;
import sg.ncl.service.registration.domain.RegistrationService;
import sg.ncl.service.registration.serializers.DateTimeDeserializer;
import sg.ncl.service.registration.serializers.DateTimeSerializer;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;
import sg.ncl.service.user.web.UsersController;
import sg.ncl.service.user.web.VerificationKeyInfo;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "flyway.enabled=false")
@ActiveProfiles("mock-registration-service")
public class RegistrationControllerTestNew extends AbstractTest {

    private final MockHttpServletRequestBuilder get = get("/registrations");
    private final MockHttpServletRequestBuilder post = post("/registrations");
    private final MockHttpServletRequestBuilder put = put("/registrations");

    @Inject
    private WebApplicationContext webApplicationContext;
    @Inject
    private RegistrationService registrationService;
    @Inject
    private ExceptionProperties properties;
    @Inject
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Mock
    private TeamService teamService;
    @Mock
    private UserService userService;
    @Mock
    private AdapterDeterLab adapterDeterLab;

    @Before
    public void before() {
        assertThat(mockingDetails(registrationService).isMock(), Matchers.is(true));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void registerNewUserJoinExistingTeamTest() throws Exception {
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
        mainJSON.put("isJoinTeam", true);

        RegistrationEntity regInfo = new RegistrationEntity();
        regInfo.setId(Long.parseLong("1234567890"));
        when(registrationService.register(
                any(CredentialsEntity.class), any(UserEntity.class), any(TeamEntity.class), anyBoolean()
        )).thenReturn(regInfo);

        MvcResult mvcResult = mockMvc.perform(post.contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isOk()).andReturn();

        assertThat(new JSONObject(mvcResult.getResponse().getContentAsString()).getString("id"), is(equalTo(regInfo.getId().toString())));
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

        RegistrationEntity regInfo = new RegistrationEntity();
        regInfo.setId(Long.parseLong("1234567890"));
        when(registrationService.register(
                any(CredentialsEntity.class), any(UserEntity.class), any(TeamEntity.class), anyBoolean()
        )).thenReturn(regInfo);

        MvcResult mvcResult = mockMvc.perform(post.contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isOk()).andReturn();

        assertThat(new JSONObject(mvcResult.getResponse().getContentAsString()).getString("id"), is(equalTo(regInfo.getId().toString())));
    }

    @Test
    public void registerOldUserApplyNewTeamTest() throws Exception {
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

        RegistrationEntity regInfo = new RegistrationEntity();
        regInfo.setId(Long.parseLong("1234567890"));

        when(registrationService.registerRequestToApplyTeam(anyString(), any(Team.class))).thenReturn(regInfo);

        MvcResult mvcResult = mockMvc.perform(post("/registrations/newTeam/" + userEntity.getId())
                .contentType(MediaType.APPLICATION_JSON).content(mainJSON.toString()))
                .andExpect(status().isCreated()).andReturn();

        assertThat(new JSONObject(mvcResult.getResponse().getContentAsString()).getString("id"), is(equalTo(regInfo.getId().toString())));
    }

    @Test
    public void verifyEmail() throws Exception {
        final VerificationKeyInfo verificationKeyInfo = new VerificationKeyInfo("key");

        final byte[] content = mapper.writeValueAsBytes(verificationKeyInfo);

        when(registrationService.verifyEmail(anyString(), anyString(), anyString())).thenReturn(true);

        mockMvc.perform(put("/registrations/users/id/emails/emailBase64").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk());
    }
}
