package sg.ncl.service.experiment.web;

/**
 * @author Te Ye
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sg.ncl.service.experiment.util.TestUtil.getExperimentEntity;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ExperimentsController.class, secure = false)
@ContextConfiguration(classes = {ExperimentsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class ExperimentsControllerTest2 {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private MockMvc mockMvc;

    @MockBean
    private ExperimentService experimentService;

    @Before
    public void before() {
        assertThat(mockingDetails(experimentService).isMock()).isTrue();
    }

    @Test
    public void testGetAllExperimentsWithNothingInDb() throws Exception {
        mockMvc.perform(get(ExperimentsController.PATH + "/experiments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddExperiment() throws Exception {
        final ExperimentInfo experimentInfo = new ExperimentInfo(0L,"","","","","","","",0,0);
        final byte[] content = mapper.writeValueAsBytes(experimentInfo);

        ExperimentEntity experimentEntity = getExperimentEntity();
        when(experimentService.save(any(ExperimentEntity.class))).thenReturn(experimentEntity);

        mockMvc.perform(post(ExperimentsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllExperiments() throws Exception {
        final List<Experiment> list = new ArrayList<>();
        final ExperimentEntity entity1 = getExperimentEntity();
        final ExperimentEntity entity2 = getExperimentEntity();
        list.add(entity1);
        list.add(entity2);

        when(experimentService.getAll()).thenReturn(list);

        mockMvc.perform(get(ExperimentsController.PATH + "/experiments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity1.getId()))))
                .andExpect(jsonPath("$[0].userId", is(equalTo(entity1.getUserId()))))
                .andExpect(jsonPath("$[0].teamId", is(equalTo(entity1.getTeamId()))))
                .andExpect(jsonPath("$[0].teamName", is(equalTo(entity1.getTeamName()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity1.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$[0].nsFile", is(equalTo(entity1.getNsFile()))))
                .andExpect(jsonPath("$[0].nsFileContent", is(equalTo(entity1.getNsFileContent()))))
                .andExpect(jsonPath("$[0].idleSwap", is(equalTo(entity1.getIdleSwap()))))
                .andExpect(jsonPath("$[0].maxDuration", is(equalTo(entity1.getMaxDuration()))))

                .andExpect(jsonPath("$[1].id", is(equalTo(entity2.getId()))))
                .andExpect(jsonPath("$[1].userId", is(equalTo(entity2.getUserId()))))
                .andExpect(jsonPath("$[1].teamId", is(equalTo(entity2.getTeamId()))))
                .andExpect(jsonPath("$[1].teamName", is(equalTo(entity2.getTeamName()))))
                .andExpect(jsonPath("$[1].name", is(equalTo(entity2.getName()))))
                .andExpect(jsonPath("$[1].description", is(equalTo(entity2.getDescription()))))
                .andExpect(jsonPath("$[1].nsFile", is(equalTo(entity2.getNsFile()))))
                .andExpect(jsonPath("$[1].nsFileContent", is(equalTo(entity2.getNsFileContent()))))
                .andExpect(jsonPath("$[1].idleSwap", is(equalTo(entity2.getIdleSwap()))))
                .andExpect(jsonPath("$[1].maxDuration", is(equalTo(entity2.getMaxDuration()))));
    }

    @Test
    public void testGetExperimentsByUserId() throws Exception {
    }

    @Test
    public void testGetExperimentsByTeamId() throws Exception {
    }

    @Test
    public void testDeleteExperimentGoodAuthenticationPrincipal() throws Exception {

    }

    @Test
    public void testDeleteExperimentBadAuthenticationPrincipalType() throws Exception {

    }

    @Test
    public void testDeleteExperimentNullAuthentication() throws Exception {

    }
}
