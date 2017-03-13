package sg.ncl.service.telemetry.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.telemetry.domain.NodeType;
import sg.ncl.service.telemetry.domain.TelemetryService;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dcsyeoty on 16-Dec-16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TelemetryController.class, secure = true)
@ContextConfiguration(classes = {TelemetryController.class})
public class TelemetryControllerTest {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private TelemetryService telemetryService;

    @Before
    public void before() {
        assertThat(mockingDetails(telemetryService).isMock()).isTrue();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetFreeNodes() throws Exception {
        String freeNodes = RandomStringUtils.randomNumeric(3);

        when(telemetryService.getNodes(NodeType.FREE)).thenReturn(freeNodes);

        mockMvc.perform(get(TelemetryController.PATH + "/nodes/counts?type=" + NodeType.FREE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.FREE", is(equalTo(freeNodes))));
    }

    @Test
    public void testGetTestbedStats() throws Exception {
        String usersCount = RandomStringUtils.randomNumeric(3);
        String runningExperimentsCount = RandomStringUtils.randomNumeric(3);

        when(telemetryService.getLoggedInUsersCount()).thenReturn(usersCount);
        when(telemetryService.getRunningExperimentsCount()).thenReturn(runningExperimentsCount);

        mockMvc.perform(get(TelemetryController.PATH + "/testbed/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.users", is(equalTo(usersCount))))
                .andExpect(jsonPath("$.experiments", is(equalTo(runningExperimentsCount))));
    }

    @Test
    public void testGetTotalNodes() throws Exception {
        String totalNodes = RandomStringUtils.randomNumeric(3);

        when(telemetryService.getNodes(NodeType.TOTAL)).thenReturn(totalNodes);

        mockMvc.perform(get(TelemetryController.PATH + "/nodes/counts?type=" + NodeType.TOTAL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.TOTAL", is(equalTo(totalNodes))));
    }
}
