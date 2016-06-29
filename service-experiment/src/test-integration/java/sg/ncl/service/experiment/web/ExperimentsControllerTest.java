package sg.ncl.service.experiment.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.experiment.AbstractTest;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Desmond.
 */
public class ExperimentsControllerTest extends AbstractTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());
    private MockMvc mockMvc;

    @Inject
    private ExperimentRepository experimentRepository;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
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
    public void testPostExperiment() throws Exception {

        ExperimentEntity experimentsEntity = Util.getExperimentsEntity();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(experimentsEntity);

        mockMvc.perform(post("/experiments").contentType(contentType).content(jsonString))
                .andExpect(status().isCreated());

        List<ExperimentEntity> experimentEntityList = experimentRepository.findAll();
        ExperimentEntity firstExperimentEntity = experimentEntityList.get(0);

        Assert.assertEquals(experimentEntityList.size(), 1);
        Assert.assertEquals(experimentsEntity.getUserId(), firstExperimentEntity.getUserId());
        Assert.assertEquals(experimentsEntity.getTeamId(), firstExperimentEntity.getTeamId());
        Assert.assertEquals(experimentsEntity.getName(), firstExperimentEntity.getName());
        Assert.assertEquals(experimentsEntity.getDescription(), firstExperimentEntity.getDescription());
        Assert.assertEquals(experimentsEntity.getNsFile(), firstExperimentEntity.getNsFile());
        Assert.assertEquals(experimentsEntity.getIdleSwap(), firstExperimentEntity.getIdleSwap());
        Assert.assertEquals(experimentsEntity.getMaxDuration(), firstExperimentEntity.getMaxDuration());
    }

    @Test
    public void testGetAllExperiments() throws Exception {

        // add 3 entries to database
        List<ExperimentEntity> experimentEntityList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            experimentEntityList.add(Util.getExperimentsEntity());
        }

        experimentEntityList = experimentRepository.save(experimentEntityList);

        // get all entries from database
        MvcResult result = mockMvc.perform(get("/experiments/experiments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String allUserJsonString = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ExperimentEntity> experimentEntityList2 = mapper.readValue(allUserJsonString, new TypeReference<List<ExperimentEntity>>(){});

        Assert.assertTrue(Util.isListEqual(experimentEntityList2, experimentEntityList));
    }

    @Test
    public void testGetExperimentsByUserId() throws Exception {

        String userId = RandomStringUtils.randomAlphanumeric(20);
        int numEntries = 6;

        Util.addExperiments(numEntries, userId, experimentRepository);

        MvcResult result = mockMvc.perform(get("/experiments/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andReturn();

        String allUserJsonString = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ExperimentEntity> experimentEntityList = mapper.readValue(allUserJsonString, new TypeReference<List<ExperimentEntity>>(){});

        Assert.assertEquals(experimentEntityList.size(), numEntries / 2);

        for (ExperimentEntity forEntity : experimentEntityList) {
            Assert.assertEquals(forEntity.getUserId(), userId);
        }

        List<ExperimentEntity> allExperiments = experimentRepository.findAll();
        Assert.assertEquals(allExperiments.size(), numEntries);
    }
}
