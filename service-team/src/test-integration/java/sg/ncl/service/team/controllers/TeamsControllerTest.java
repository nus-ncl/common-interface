package sg.ncl.service.team.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.*;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.dtos.TeamInfo;

import javax.inject.Inject;
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
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

        Gson gson = Converters.registerAll(new GsonBuilder()).create();
        String json = gson.toJson(teamInfo);

        mockMvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + json);

        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype());

        MvcResult mvcResult = mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String list = mvcResult.getResponse().getContentAsString();

        JSONArray jsonArray =  new JSONArray(list);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        Assert.assertEquals(jsonObject.getString("name"), teamInfo.getName());
        Assert.assertEquals(jsonObject.getString("description"), teamInfo.getDescription());
        Assert.assertEquals(jsonObject.getString("status"), "PENDING");
    }

    private TeamEntity createTeam() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        return teamEntity;
    }

}


