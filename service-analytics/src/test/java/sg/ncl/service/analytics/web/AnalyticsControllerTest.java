package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.ProjectDetails;
import sg.ncl.service.analytics.domain.ProjectService;
import sg.ncl.service.analytics.util.TestUtil;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author Tran Ly Vu
 */

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AnalyticsController.class, secure = true)
@ContextConfiguration(classes = {AnalyticsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = "flyway.enabled=false")
public class AnalyticsControllerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Inject
    private ObjectMapper mapper;
    @Inject
    private WebApplicationContext webApplicationContext;
    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;
    @MockBean
    private ProjectService projectService;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(analyticsService).isMock()).isTrue();
        assertThat(mockingDetails(projectService).isMock()).isTrue();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getDataDownloadCountUnauthorizedException() throws Exception {
        when(authentication.getPrincipal()).thenReturn(claims);

        try {
            mockMvc.perform(get(AnalyticsController.PATH + "/datasets/downloads"));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(UnauthorizedException.class);
        }
    }

    @Test
    public void getDataDownloadCountGood() throws Exception {

        List<DataDownloadStatistics> randomList = new ArrayList<>();
        Random rand = new Random();
        int randomSize = rand.nextInt(10) + 1;;

        for (int i =0; i <  randomSize; i++) {
            Long randomId =  new Random().nextLong();
            Long randomCount = new Random().nextLong();
            DataDownloadStatistics dataDownloadStatistics = new DataDownloadStatistics(randomId, randomCount);
            randomList.add(dataDownloadStatistics);
        }

        when(analyticsService.getDataDownloadCount(anyLong(),any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(randomList);

        mockMvc.perform(get(AnalyticsController.PATH + "/datasets/downloads"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUsageStatisticsUnauthorizedException() throws Exception {
        when(authentication.getPrincipal()).thenReturn(claims);

        try {
            mockMvc.perform(get(AnalyticsController.PATH + "/usage/teams"));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(UnauthorizedException.class);
        }
    }

    @Test
    public void testGetUsageStatisticsGood() throws Exception {

        String randomUsage = RandomStringUtils.randomAlphanumeric(10);
        String randomId =  RandomStringUtils.randomAlphanumeric(10);

        when(analyticsService.getUsageStatistics(anyString(),any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(randomUsage);

        mockMvc.perform(get(AnalyticsController.PATH + "/usage/teams/" + randomId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEnergyStatistics() throws Exception {
        final List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.getAuthority());
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        List<Double> randomList = new ArrayList<>();
        Random rand = new Random();
        int randomSize = rand.nextInt(10) + 1;

        for (int i =0; i <  randomSize; i++) {
            double randomEnergy = Double.parseDouble(RandomStringUtils.randomNumeric(10));
            randomList.add(randomEnergy);
        }

        when(analyticsService.getEnergyStatistics(any(ZonedDateTime.class), any(ZonedDateTime.class)))
                    .thenReturn(randomList);

        mockMvc.perform(get(AnalyticsController.PATH + "/energy"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEnergyStatisticsForbiddenException() throws Exception {
        final List<String> roles = new ArrayList<>();
        roles.add(Role.USER.getAuthority());
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        mockMvc.perform(get(AnalyticsController.PATH + "/energy"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAllProjectDetails() throws Exception {
        final List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.getAuthority());
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        Random rand = new Random();
        int randomSize = rand.nextInt(10) + 1;

        for (int i =0; i <  randomSize; i++) {
            projectDetailsList.add(TestUtil.getProjectDetailsEntity());
        }

        when(projectService.getAllProjectDetails()).thenReturn(projectDetailsList);

        mockMvc.perform(get(AnalyticsController.PATH + "/usage/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
