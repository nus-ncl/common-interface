package sg.ncl.service.team.controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.dtos.TeamMemberInfo;
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

/**
 * Created by Desmond / Te Ye
 */
public class TeamsControllerTest extends AbstractTest {
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
        String jsonInString = gson.toJson(teamEntity);

        mockMvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
                .andExpect(status().isCreated());

        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        MvcResult mvcResult = mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String list = mvcResult.getResponse().getContentAsString();

        Type listType = new TypeToken<ArrayList<TeamEntity>>(){}.getType();
        List<TeamEntity> teamEntityList2 = gson.fromJson(list, listType);

        TeamEntity grabTeamEntity = teamEntityList2.get(0);

        Assert.assertEquals(teamEntity.getName(), grabTeamEntity.getName());
        Assert.assertEquals(teamEntity.getDescription(), grabTeamEntity.getDescription());
        Assert.assertEquals(teamEntity.getStatus(), TeamStatus.PENDING);
//        Assert.assertEquals(teamEntity.getApplicationDate(), grabTeamEntity.getApplicationDate());
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        MvcResult result = mockMvc.perform(get("/teams")).andReturn();
        Assert.assertTrue(result.getResponse().getContentLength() == 0);
    }

    @Test
    public void testGetTeamWithNoUserInDb() throws Exception {
        mockMvc.perform(get("/teams/" + RandomStringUtils.randomAlphabetic(20)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Team not found"));
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
        mockMvc.perform(post("/teams/addUserToTeam/" + teamId).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
                .andExpect(status().isOk());

        // get team after add user
        mockMvc.perform(get("/teams/" + teamId))
                .andExpect(status().isOk());
    }

    @Test
    public void testPutTeam() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        TeamEntity origTeamEntity = Util.getTeamEntity();
        TeamEntity teamEntity = teamRepository.save(origTeamEntity);
        final String idString = teamEntity.getId();

        // get user
        MvcResult result = mockMvc.perform(get("/teams/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // parse JSON into TeamEntity
        TeamEntity teamEntityFromDb = gson.fromJson(result.getResponse().getContentAsString(), TeamEntity.class);

        Assert.assertEquals(origTeamEntity.getName(), teamEntityFromDb.getName());
        Assert.assertEquals(origTeamEntity.getDescription(), teamEntityFromDb.getDescription());

        String name = teamEntityFromDb.getName();
        String description = teamEntityFromDb.getDescription();

        // change name
        String newDescription = RandomStringUtils.randomAlphabetic(20);
        teamEntityFromDb.setDescription(newDescription);

        String jsonString = gson.toJson(teamEntityFromDb);

        // put
        mockMvc.perform(put("/teams/" + idString).contentType(contentType).content(jsonString))
                .andExpect(status().isAccepted());

        // check if name is new name and description is the same
        mockMvc.perform(get("/teams/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(newDescription)));
    }

    @Test
    public void testPutTeamWithWrongId() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final String idString = "123456";

        // put
        mockMvc.perform(put("/teams/" + idString).contentType(contentType).content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Team ID is empty"));
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

        MvcResult mvcResult = mockMvc.perform(get("/teams/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        String list = mvcResult.getResponse().getContentAsString();

        Type listType = new TypeToken<ArrayList<TeamEntity>>(){}.getType();
        List<TeamEntity> teamEntityList = gson.fromJson(list, listType);

        Assert.assertThat(teamEntityList.size(), is(1));
        Assert.assertThat(teamEntityList.get(0).getVisibility(), is(TeamVisibility.PUBLIC));
    }

    @Test
    public void testGetByName() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        String name = RandomStringUtils.randomAlphanumeric(20);
        teamEntity.setName(name);

        teamRepository.save(teamEntity);

        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        MvcResult mvcResult = mockMvc.perform(get("/teams/name/" + name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        String list = mvcResult.getResponse().getContentAsString();
        TeamEntity entity = gson.fromJson(list, TeamEntity.class);

        Assert.assertThat(entity.getName(), is(name));
    }
}


