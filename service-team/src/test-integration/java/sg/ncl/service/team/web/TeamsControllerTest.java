package sg.ncl.service.team.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.serializers.DateTimeDeserializer;
import sg.ncl.service.team.serializers.DateTimeSerializer;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static sg.ncl.service.team.Checks.checkException;

/**
 * Created by Desmond / Te Ye
 */
@WebAppConfiguration
public class TeamsControllerTest extends AbstractTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Inject
    private TeamRepository teamRepository;

    private MockMvc mockMvc;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testPostTeam() throws Exception {
        // Note: must have TeamEntity to create the JSON
        TeamEntity teamEntity = Util.getTeamEntity();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String jsonInString = gson.toJson(new TeamInfo(teamEntity));

        MvcResult createdTeamResult = mockMvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
                .andExpect(status().isCreated())
                .andReturn();

        String response = createdTeamResult.getResponse().getContentAsString();
        Team createdTeam = gson.fromJson(response, TeamInfo.class);

        Assert.assertEquals(teamEntity.getName(), createdTeam.getName());
        Assert.assertEquals(teamEntity.getDescription(), createdTeam.getDescription());
        Assert.assertEquals(teamEntity.getStatus(), TeamStatus.PENDING);
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        MvcResult result = mockMvc.perform(get("/teams")).andReturn();
        Assert.assertTrue(result.getResponse().getContentLength() == 0);
    }

    @Test
    public void testGetTeamWithNoUserInDb() throws Exception {
        try {
            mockMvc.perform(get("/teams/" + RandomStringUtils.randomAlphabetic(20)))
                    .andExpect(status().isNotFound())
                    .andExpect(status().reason("Team not found"));
        } catch (Exception e) {
            checkException(e, "");
            return;
        }
        exception.expect(TeamNotFoundException.class);
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
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(originalEntity.getName())));
    }

    @Test
    public void testGetTeamAfterAddUsertoTeam() throws Exception {
        TeamEntity origTeamEntity = Util.getTeamEntity();
        TeamEntity teamEntity = teamRepository.save(origTeamEntity);
        String teamId = teamEntity.getId();

        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo();
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
    public void testPutTeam() throws Exception {
        TeamEntity origTeamEntity = Util.getTeamEntity();
        TeamEntity savedTeamEntity = teamRepository.save(origTeamEntity);
        final String id = savedTeamEntity.getId();

        TeamEntity teamEntityFromDb = teamRepository.findOne(id);

        // change name
        String editedName = RandomStringUtils.randomAlphanumeric(20);
        String editedDescription = RandomStringUtils.randomAlphanumeric(20);
        teamEntityFromDb.setName(editedName);
        teamEntityFromDb.setDescription(editedDescription);

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // convert to TeamInfo
        // put
        String jsonString = gson.toJson(new TeamInfo(teamEntityFromDb));

        mockMvc.perform(put("/teams/" + id).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isAccepted());

        // check if name is new name and description is the same
        mockMvc.perform(get("/teams/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(editedName)))
                .andExpect(jsonPath("$.description", is(editedDescription)));
    }

    @Test
    public void testPutTeamWithWrongId() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        final String idString = "123456";

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // put
        try {
            mockMvc.perform(put("/teams/" + idString).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(new TeamInfo(teamEntity))))
                    .andExpect(status().isNotFound())
                    .andExpect(status().reason("Team not found"));
        } catch (Exception e) {
            checkException(e, idString);
            return;
        }
        exception.expect(TeamNotFoundException.class);
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
                .andExpect(content().contentType(contentType))
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
                .andExpect(content().contentType(contentType))
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

