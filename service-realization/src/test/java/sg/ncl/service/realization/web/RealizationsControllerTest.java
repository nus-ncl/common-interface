package sg.ncl.service.realization.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationService;

import javax.inject.Inject;

import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Desmond.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RealizationsController.class, secure = true)
@ContextConfiguration(classes = {RealizationsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = "flyway.enabled=false")
public class RealizationsControllerTest {

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
    private RealizationService realizationService;

    @Before
    public void before() {
        Assertions.assertThat(mockingDetails(claims).isMock()).isTrue();
        Assertions.assertThat(mockingDetails(securityContext).isMock()).isTrue();
        Assertions.assertThat(mockingDetails(authentication).isMock()).isTrue();
        Assertions.assertThat(mockingDetails(realizationService).isMock()).isTrue();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetRealization() throws Exception {
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(realizationService.getByExperimentId(Long.parseLong(experimentId))).thenReturn(new RealizationEntity());

        mockMvc.perform(get(RealizationsController.PATH + "/" + experimentId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRealizationWithTeamName() throws Exception {
        final String teamName = RandomStringUtils.randomAlphabetic(8);
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(realizationService.getByExperimentId(teamName, Long.parseLong(experimentId))).thenReturn(new RealizationEntity());

        mockMvc.perform(get(RealizationsController.PATH + "/team/" + teamName + "/experiment/" + experimentId))
                .andExpect(status().isOk());
    }

    @Test
    public void testStartExperiment() throws Exception {
        final String teamName = RandomStringUtils.randomAlphanumeric(8);
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(realizationService.getByExperimentId(Long.parseLong(experimentId))).thenReturn(new RealizationEntity());

        mockMvc.perform(post(RealizationsController.PATH + "/start/team/" + teamName + "/experiment/" + experimentId))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testStartExperimentForbidden() throws Exception {
        final String teamName = RandomStringUtils.randomAlphanumeric(8);
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);

        mockMvc.perform(post(RealizationsController.PATH + "/start/team/" + teamName + "/experiment/" + experimentId))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testStopExperiment() throws Exception {
        final String teamName = RandomStringUtils.randomAlphanumeric(8);
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(realizationService.getByExperimentId(Long.parseLong(experimentId))).thenReturn(new RealizationEntity());

        mockMvc.perform(post(RealizationsController.PATH + "/stop/team/" + teamName + "/experiment/" + experimentId))
                .andExpect(status().isAccepted());
    }
}
