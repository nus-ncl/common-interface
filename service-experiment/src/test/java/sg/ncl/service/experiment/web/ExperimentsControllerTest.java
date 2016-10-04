//package sg.ncl.service.experiment.web;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import io.jsonwebtoken.Claims;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.web.context.WebApplicationContext;
//import sg.ncl.service.experiment.AbstractTest;
//import sg.ncl.service.experiment.Util;
//import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
//import sg.ncl.service.experiment.domain.Experiment;
//import sg.ncl.service.experiment.domain.ExperimentService;
//
//import javax.inject.Inject;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//import static org.mockito.Matchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockingDetails;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
///**
// * Created by Desmond.
// */
//@ActiveProfiles({"mock-experiment-service"})
//public class ExperimentsControllerTest extends AbstractTest {
//
//    @Rule
//    public MockitoRule mockito = MockitoJUnit.rule();
//
//    @Rule
//    public final ExpectedException exception = ExpectedException.none();
//
//    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());
//    private MockMvc mockMvc;
//
//    @Inject
//    private ExperimentService experimentService;
//
//    @Inject
//    private WebApplicationContext webApplicationContext;
//
//    @Before
//    public void setUp() throws Exception {
//        assertThat(mockingDetails(experimentService).isMock(), is(true));
//        mockMvc = webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    public void testGetAllExperimentsWithNothingInDb() throws Exception {
//        MvcResult result = mockMvc.perform(get("/experiments/experiments")
//                .contentType(contentType))
//                .andReturn();
//        Assert.assertTrue(result.getResponse().getContentLength() == 0);
//    }
//
//    @Test
//    public void testAddExperiment() throws Exception {
//
//        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
//        when(experimentService.save(any())).thenReturn(experimentEntity);
//
//        ExperimentEntity experimentEntity2 = Util.getExperimentsEntity();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        String jsonString = mapper.writeValueAsString(experimentEntity2);
//
//        mockMvc.perform(post("/experiments").contentType(contentType).content(jsonString))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void testGetAllExperiments() throws Exception {
//
//        // add 3 entries to database
//        List<Experiment> experimentEntityList = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            experimentEntityList.add(Util.getExperimentsEntity());
//        }
//
//        // mocking the getAll all experiments
//        when(experimentService.getAll()).thenReturn(experimentEntityList);
//
//        // getAll all entries from database
//        mockMvc.perform(get("/experiments/experiments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(contentType));
//    }
//
//    @Test
//    public void testGetExperimentsByUserId() throws Exception {
//
//        List<Experiment> experimentEntityList = new ArrayList<>();
//        String userId = RandomStringUtils.randomAlphanumeric(20);
//
//        for (int i = 0; i < 3; i++) {
//            experimentEntityList.add(Util.getExperimentsEntity());
//        }
//
//        when(experimentService.findByUser(userId)).thenReturn(experimentEntityList);
//
//        mockMvc.perform(get("/experiments/users/" + userId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(contentType));
//    }
//
//    @Test
//    public void testGetExperimentsByTeamId() throws Exception {
//
//        List<Experiment> experimentEntityList = new ArrayList<>();
//        String teamId = RandomStringUtils.randomAlphanumeric(20);
//
//        for (int i = 0; i < 3; i++) {
//            experimentEntityList.add(Util.getExperimentsEntity());
//        }
//
//        when(experimentService.findByTeam(teamId)).thenReturn(experimentEntityList);
//
//        mockMvc.perform(get("/experiments/teams/" + teamId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(contentType));
//    }
//
//    @Test
//    public void testDeleteExperimentGoodAuthenticationPrincipal() throws Exception {
//        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
//        String teamId = RandomStringUtils.randomAlphabetic(8);
//        final Claims claims = mock(Claims.class);
//
//        Authentication authentication = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(authentication.getPrincipal()).thenReturn(claims);
//        when(experimentService.deleteExperiment(eq(experimentId), eq(teamId), any(Claims.class))).thenReturn(Util.getExperimentsEntity());
//
//        mockMvc.perform(delete("/experiments/" + experimentId + "/teams/" + teamId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testDeleteExperimentBadAuthenticationPrincipalType() throws Exception {
//        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
//        String teamId = RandomStringUtils.randomAlphabetic(8);
//
//        Authentication authentication = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(authentication.getPrincipal()).thenReturn("");
//        when(experimentService.deleteExperiment(eq(experimentId), eq(teamId), any(Claims.class))).thenReturn(Util.getExperimentsEntity());
//
//        mockMvc.perform(delete("/experiments/" + experimentId + "/teams/" + teamId))
//                .andExpect(status().isForbidden());
//    }
//
//    // TODO add a test when securityContext.getAuthentication is null should be identical result as claims is not instance of Arraylist
//}
