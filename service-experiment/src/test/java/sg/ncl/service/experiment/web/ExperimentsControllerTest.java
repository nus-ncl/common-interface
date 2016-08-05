package sg.ncl.service.experiment.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.service.experiment.AbstractTest;
import sg.ncl.service.experiment.ExperimentConnectionProperties;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.logic.ExperimentService;
import sg.ncl.service.realization.data.jpa.RealizationEntity;

import javax.inject.Inject;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Desmond.
 */
@ActiveProfiles({"mock-experiment-service"})
public class ExperimentsControllerTest extends AbstractTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());
    private MockMvc mockMvc;

    @Inject
    private ExperimentService experimentService;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        assertThat(mockingDetails(experimentService).isMock(), is(true));
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllExperimentsWithNothingInDb() throws Exception {
        MvcResult result = mockMvc.perform(get("/experiments/experiments")
                .contentType(contentType))
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentLength() == 0);
    }

    @Test
    public void testAddExperiment() throws Exception {

        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
        when(experimentService.save(Matchers.any())).thenReturn(experimentEntity);

        ExperimentEntity experimentEntity2 = Util.getExperimentsEntity();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(experimentEntity2);

        mockMvc.perform(post("/experiments").contentType(contentType).content(jsonString))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAllExperiments() throws Exception {

        // add 3 entries to database
        List<ExperimentEntity> experimentEntityList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            experimentEntityList.add(Util.getExperimentsEntity());
        }

        // mocking the get all experiments
        when(experimentService.get()).thenReturn(experimentEntityList);

        // get all entries from database
        mockMvc.perform(get("/experiments/experiments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void testGetExperimentsByUserId() throws Exception {

        List<ExperimentEntity> experimentEntityList = new ArrayList<>();
        String userId = RandomStringUtils.randomAlphanumeric(20);

        for (int i = 0; i < 3; i++) {
            experimentEntityList.add(Util.getExperimentsEntity());
        }

        when(experimentService.findByUser(userId)).thenReturn(experimentEntityList);

        mockMvc.perform(get("/experiments/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void testGetExperimentsByTeamId() throws Exception {

        List<ExperimentEntity> experimentEntityList = new ArrayList<>();
        String teamId = RandomStringUtils.randomAlphanumeric(20);

        for (int i = 0; i < 3; i++) {
            experimentEntityList.add(Util.getExperimentsEntity());
        }

        when(experimentService.findByTeam(teamId)).thenReturn(experimentEntityList);

        mockMvc.perform(get("/experiments/teams/" + teamId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void testDeleteExperiment() throws Exception {
        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));

        when(experimentService.deleteExperiment(experimentId)).thenReturn("Experiment deleted.");

        mockMvc.perform(post("/experiments/delete/" + experimentId))
                .andExpect(status().isOk());
    }
}
