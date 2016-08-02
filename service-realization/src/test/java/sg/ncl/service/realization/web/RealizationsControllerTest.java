package sg.ncl.service.realization.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.realization.AbstractTest;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.logic.RealizationService;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Desmond.
 */
@ActiveProfiles("mock-realization-service")
public class RealizationsControllerTest extends AbstractTest {

    @Inject
    private WebApplicationContext webApplicationContext;

    @Inject
    private RealizationService realizationService;

    private MockMvc mockMvc;

    @Before
    public void before() {
        assertThat(mockingDetails(realizationService).isMock(), is(true));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetRealization() throws Exception {
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(realizationService.getByExperimentId(Long.parseLong(experimentId))).thenReturn(new RealizationEntity());

        mockMvc.perform(get("/realizations/" + experimentId))
                .andExpect(status().isOk());
    }

    @Test
    public void testStartExperiment() throws Exception {
        final String teamName = RandomStringUtils.randomAlphanumeric(8);
        final String experimentId = RandomStringUtils.randomNumeric(5);
        final String userId = RandomStringUtils.randomAlphanumeric(20);

        when(realizationService.getByExperimentId(Long.parseLong(experimentId))).thenReturn(new RealizationEntity());
//        when(realizationService.startExperimentInDeter(teamName, experimentId, userId)).thenReturn("");

        mockMvc.perform(post("/realizations/start/team/" + teamName + "/experiment/" + experimentId))
                .andExpect(status().isOk());
    }

    @Test
    public void testStopExperiment() throws Exception {
        final String teamName = RandomStringUtils.randomAlphanumeric(8);
        final String experimentId = RandomStringUtils.randomNumeric(5);

        when(realizationService.getByExperimentId(Long.parseLong(experimentId))).thenReturn(new RealizationEntity());

        mockMvc.perform(post("/realizations/stop/team/" + teamName + "/experiment/" + experimentId))
                .andExpect(status().isOk());
    }
}
