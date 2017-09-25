package sg.ncl.service.experiment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sg.ncl.service.experiment.util.TestUtil.getExperimentEntity;

/**
 * @author Te Ye
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ExperimentsController.class, secure = true)
@ContextConfiguration(classes = {ExperimentsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class ExperimentsControllerTest {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @MockBean
    private ExperimentService experimentService;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(experimentService).isMock()).isTrue();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllExperimentsWithNothingInDb() throws Exception {
        mockMvc.perform(get(ExperimentsController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddExperiment() throws Exception {
        final ExperimentInfo experimentInfo = new ExperimentInfo(0L,"","","","","","","",0,0, ZonedDateTime.now(), ZonedDateTime.now());
        final byte[] content = mapper.writeValueAsBytes(experimentInfo);

        ExperimentEntity entity1 = getExperimentEntity();
        when(experimentService.save(any(ExperimentEntity.class))).thenReturn(entity1);

        mockMvc.perform(post(ExperimentsController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.userId", is(equalTo(entity1.getUserId()))))
                .andExpect(jsonPath("$.teamId", is(equalTo(entity1.getTeamId()))))
                .andExpect(jsonPath("$.teamName", is(equalTo(entity1.getTeamName()))))
                .andExpect(jsonPath("$.name", is(equalTo(entity1.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$.nsFile", is(equalTo(entity1.getNsFile()))))
                .andExpect(jsonPath("$.nsFileContent", is(equalTo(entity1.getNsFileContent()))))
                .andExpect(jsonPath("$.idleSwap", is(equalTo(entity1.getIdleSwap()))))
                .andExpect(jsonPath("$.maxDuration", is(equalTo(entity1.getMaxDuration()))));
    }

    @Test
    public void testGetAllExperiments() throws Exception {
        final List<Experiment> list = new ArrayList<>();
        final ExperimentEntity entity1 = getExperimentEntity();
        final ExperimentEntity entity2 = getExperimentEntity();
        list.add(entity1);
        list.add(entity2);

        when(experimentService.getAll()).thenReturn(list);

        mockMvc.perform(get(ExperimentsController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].userId", is(equalTo(entity1.getUserId()))))
                .andExpect(jsonPath("$[0].teamId", is(equalTo(entity1.getTeamId()))))
                .andExpect(jsonPath("$[0].teamName", is(equalTo(entity1.getTeamName()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity1.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$[0].nsFile", is(equalTo(entity1.getNsFile()))))
                .andExpect(jsonPath("$[0].nsFileContent", is(equalTo(entity1.getNsFileContent()))))
                .andExpect(jsonPath("$[0].idleSwap", is(equalTo(entity1.getIdleSwap()))))
                .andExpect(jsonPath("$[0].maxDuration", is(equalTo(entity1.getMaxDuration()))))

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
        final List<Experiment> list = new ArrayList<>();
        final ExperimentEntity entity1 = getExperimentEntity();
        list.add(entity1);

        when(experimentService.findByUser(anyString())).thenReturn(list);

        mockMvc.perform(get(ExperimentsController.PATH + "/users/id").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].userId", is(equalTo(entity1.getUserId()))))
                .andExpect(jsonPath("$[0].teamId", is(equalTo(entity1.getTeamId()))))
                .andExpect(jsonPath("$[0].teamName", is(equalTo(entity1.getTeamName()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity1.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$[0].nsFile", is(equalTo(entity1.getNsFile()))))
                .andExpect(jsonPath("$[0].nsFileContent", is(equalTo(entity1.getNsFileContent()))))
                .andExpect(jsonPath("$[0].idleSwap", is(equalTo(entity1.getIdleSwap()))))
                .andExpect(jsonPath("$[0].maxDuration", is(equalTo(entity1.getMaxDuration()))));
    }

    @Test
    public void testGetExperimentsByTeamId() throws Exception {
        final List<Experiment> list = new ArrayList<>();
        final ExperimentEntity entity1 = getExperimentEntity();
        list.add(entity1);

        when(experimentService.findByTeam(anyString())).thenReturn(list);

        mockMvc.perform(get(ExperimentsController.PATH + "/teams/id").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].userId", is(equalTo(entity1.getUserId()))))
                .andExpect(jsonPath("$[0].teamId", is(equalTo(entity1.getTeamId()))))
                .andExpect(jsonPath("$[0].teamName", is(equalTo(entity1.getTeamName()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity1.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$[0].nsFile", is(equalTo(entity1.getNsFile()))))
                .andExpect(jsonPath("$[0].nsFileContent", is(equalTo(entity1.getNsFileContent()))))
                .andExpect(jsonPath("$[0].idleSwap", is(equalTo(entity1.getIdleSwap()))))
                .andExpect(jsonPath("$[0].maxDuration", is(equalTo(entity1.getMaxDuration()))));
    }

    @Test
    public void testGetExperimentDetails() throws Exception {
        String json = RandomStringUtils.randomAlphanumeric(20);

        when(experimentService.getExperimentDetails(anyString(), anyLong())).thenReturn(json);

        mockMvc.perform(get(ExperimentsController.PATH + "/teams/teamId/experiments/" + 1L + "/experimentDetails").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(equalTo(json))));
    }

    @Test
    public void testDeleteExperimentGoodAuthenticationPrincipal() throws Exception {
        final ExperimentEntity entity = getExperimentEntity();
        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String teamId = RandomStringUtils.randomAlphabetic(8);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);

        when(experimentService.deleteExperiment(anyLong(), anyString(), any(Claims.class))).thenReturn(entity);

        mockMvc.perform(delete(ExperimentsController.PATH + "/teams/" + teamId + "/experiments/" + experimentId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.userId", is(equalTo(entity.getUserId()))))
                .andExpect(jsonPath("$.teamId", is(equalTo(entity.getTeamId()))))
                .andExpect(jsonPath("$.teamName", is(equalTo(entity.getTeamName()))))
                .andExpect(jsonPath("$.name", is(equalTo(entity.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(entity.getDescription()))))
                .andExpect(jsonPath("$.nsFile", is(equalTo(entity.getNsFile()))))
                .andExpect(jsonPath("$.nsFileContent", is(equalTo(entity.getNsFileContent()))))
                .andExpect(jsonPath("$.idleSwap", is(equalTo(entity.getIdleSwap()))))
                .andExpect(jsonPath("$.maxDuration", is(equalTo(entity.getMaxDuration()))));
    }

    @Test
    public void testDeleteExperimentBadAuthenticationPrincipalType() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("");

        when(experimentService.deleteExperiment(anyLong(), anyString(), any(Claims.class))).thenReturn(null);

        mockMvc.perform(delete(ExperimentsController.PATH + "/teams/" + "teamId" + "/experiments/" + 1L))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteExperimentNullAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);

        when(experimentService.deleteExperiment(anyLong(), anyString(), any(Claims.class))).thenReturn(null);

        mockMvc.perform(delete(ExperimentsController.PATH + "/teams/" + "teamId" + "/experiments/" + 1L))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
