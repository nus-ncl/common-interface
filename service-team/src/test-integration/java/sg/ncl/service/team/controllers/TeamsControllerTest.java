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
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.serializers.DateTimeDeserializer;
import sg.ncl.service.team.serializers.DateTimeSerializer;
import sg.ncl.service.team.serializers.GsonDateTimeModule;

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
    public void postTeamTest() throws Exception {
        // Note: must have TeamEntity to create the JSON
        TeamEntity teamInfo = createTeam();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(teamInfo);

        mockMvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(json))
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

        Assert.assertEquals(teamInfo.getName(), grabTeamEntity.getName());
        Assert.assertEquals(teamInfo.getDescription(), grabTeamEntity.getDescription());
        Assert.assertEquals(teamInfo.getStatus(), TeamStatus.PENDING);
        Assert.assertEquals(teamInfo.getApplicationDate(), grabTeamEntity.getApplicationDate());
    }

    @Test
    public void getAllTeamsWithNoUserInDbTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/teams")).andReturn();
        Assert.assertTrue(result.getResponse().getContentLength() == 0);
    }

    @Test
    public void getTeamWithNoUserInDbTest() throws Exception {
        mockMvc.perform(get("/teams/" + RandomStringUtils.randomAlphabetic(20)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Team not found"));
    }

    @Test
    public void getTeamTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        TeamEntity teamEntity = createTeam();
        teamEntity = teamRepository.save(teamEntity);

        final String idString = teamEntity.getId();
        final TeamEntity originalEntity = teamEntity;

        mockMvc.perform(get("/teams/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(originalEntity.getName())));
    }

    @Test
    public void putTeamTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        TeamEntity origTeamEntity = createTeam();
        TeamEntity teamEntity = teamRepository.save(origTeamEntity);
        final String idString = teamEntity.getId();

        // get user
        MvcResult result = mockMvc.perform(get("/teams/" + idString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        // create GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeSerializer());
//        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new DateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new GsonDateTimeModule());
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
    public void putTeamWithWrongIdTest() throws Exception {
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        final String idString = "123456";

        // put
        mockMvc.perform(put("/teams/" + idString).contentType(contentType).content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Team ID is empty"));
    }

    private TeamEntity createTeam() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        return teamEntity;
    }

}


