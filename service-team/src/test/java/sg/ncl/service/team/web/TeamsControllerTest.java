package sg.ncl.service.team.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.serializers.DateTimeDeserializer;
import sg.ncl.service.team.serializers.DateTimeSerializer;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Desmond / Te Ye
 */
@WebAppConfiguration
@TestPropertySource(properties = "flyway.enabled=false")
public class TeamsControllerTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private TeamRepository teamRepository;

    private MockMvc mockMvc;
    private Claims claims;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        // need this because controller checks for authentication for some endpoints
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        claims = mock(Claims.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(claims);
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        MvcResult result = mockMvc.perform(get("/teams")).andReturn();
        Assert.assertTrue(result.getResponse().getContentLength() == 0);
    }

    @Test
    public void testGetTeamWithNoUserInDb() throws Exception {
        mockMvc.perform(get("/teams/" + RandomStringUtils.randomAlphabetic(20)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTeam() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity = teamRepository.save(teamEntity);

        final String idString = teamEntity.getId();
        final TeamEntity originalEntity = teamEntity;

        mockMvc.perform(get("/teams/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.name", is(originalEntity.getName())));
    }

    @Test
    public void testGetTeamAfterAddUsertoTeam() throws Exception {
        TeamEntity origTeamEntity = Util.getTeamEntity();
        TeamEntity teamEntity = teamRepository.save(origTeamEntity);
        String teamId = teamEntity.getId();

        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.MEMBER);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String jsonInString = gson.toJson(teamMemberInfo);

        // get team before add user
        mockMvc.perform(get("/teams/" + teamId))
                .andExpect(status().isOk());

        // add user to team
        mockMvc.perform(post("/teams/" + teamId + "/members").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
                .andExpect(status().isCreated());

        // assert that team has a member
        // get team after add user
        MvcResult mvcResult = mockMvc.perform(get("/teams/" + teamId))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Team resultTeam = gson.fromJson(response, TeamInfo.class);

        Assert.assertThat(teamId, is(resultTeam.getId()));
        Assert.assertThat(teamMemberInfo.getUserId(), is(resultTeam.getMembers().get(0).getUserId()));
//        Assert.assertThat(teamMemberInfo.getJoinedDate(), is(resultTeam.getMembers().get(0).getJoinedDate()));
        Assert.assertThat(teamMemberInfo.getMemberType(), is(resultTeam.getMembers().get(0).getMemberType()));
    }

    @Test
    public void testUpdateTeamDetailsGood() throws Exception {
        TeamEntity origTeamEntity = Util.getTeamEntity();
        TeamEntity savedTeamEntity = teamRepository.save(origTeamEntity);
        final String id = savedTeamEntity.getId();

        TeamEntity teamEntityFromDb = teamRepository.findOne(id);

        // change name
        String editedName = RandomStringUtils.randomAlphanumeric(20);
        String editedDescription = RandomStringUtils.randomAlphanumeric(20);
        String editedWebsite = "http://" + RandomStringUtils.randomAlphabetic(8) + ".com";
        teamEntityFromDb.setName(editedName);
        teamEntityFromDb.setDescription(editedDescription);
        teamEntityFromDb.setWebsite(editedWebsite);

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // convert to TeamInfo
        // put
        String jsonString = gson.toJson(new TeamInfo(teamEntityFromDb));

        mockMvc.perform(put("/teams/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk());

        // check if name is new name and description is the same
        mockMvc.perform(get("/teams/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(editedName)))
                .andExpect(jsonPath("$.description", is(editedDescription)))
                .andExpect(jsonPath("$.website", is(editedWebsite)));
    }

    @Test
    public void testUpdateTeamDetailsWithWrongTeamId() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        final String idString = "123456";

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // put
        mockMvc.perform(put("/teams/" + idString).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(new TeamInfo(teamEntity))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTeamStatusRestricted() throws Exception {
        TeamEntity origTeamEntity = Util.getTeamEntity();
        origTeamEntity.setStatus(TeamStatus.APPROVED);
        TeamEntity savedTeamEntity = teamRepository.save(origTeamEntity);
        final String id = savedTeamEntity.getId();

        final List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.getAuthority());
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // put
        mockMvc.perform(put("/teams/" + id + "/status/" + TeamStatus.RESTRICTED).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(new TeamInfo(savedTeamEntity))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", Matchers.is(equalTo(id))))
                .andExpect(jsonPath("$.status", Matchers.is(equalTo(TeamStatus.RESTRICTED.toString()))));;
    }

    @Test
    public void testGetPublicTeams() throws Exception {
        TeamEntity origTeamEntity = Util.getTeamEntity();

        TeamEntity privateTeamEntity = Util.getTeamEntity();
        privateTeamEntity.setVisibility(TeamVisibility.PRIVATE);

        teamRepository.save(origTeamEntity);
        teamRepository.save(privateTeamEntity);

        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        MvcResult mvcResult = mockMvc.perform(get("/teams/?visibility=PUBLIC"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andReturn();

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        String list = mvcResult.getResponse().getContentAsString();

        Type listType = new TypeToken<ArrayList<TeamInfo>>() {
        }.getType();
        List<Team> teamList = gson.fromJson(list, listType);

        Assert.assertThat(teamList.size(), is(1));
        Assert.assertThat(teamList.get(0).getVisibility(), is(TeamVisibility.PUBLIC));
    }

    @Test
    public void testGetByName() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        String name = RandomStringUtils.randomAlphanumeric(20);
        teamEntity.setName(name);

        teamRepository.save(teamEntity);

        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        MvcResult mvcResult = mockMvc.perform(get("/teams?name=" + name))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andReturn();

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        String list = mvcResult.getResponse().getContentAsString();
        Team entity = gson.fromJson(list, TeamInfo.class);

        Assert.assertThat(entity.getName(), is(name));
    }
}


