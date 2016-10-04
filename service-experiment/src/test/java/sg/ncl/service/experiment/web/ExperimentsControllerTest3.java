package sg.ncl.service.experiment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.experiment.domain.ExperimentService;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Te Ye
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ExperimentsController.class, secure = false)
@ContextConfiguration(classes = {ExperimentsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class ExperimentsControllerTest3 {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Inject
    private ObjectMapper mapper;
//    @Inject
//    private MockMvc mockMvc;
    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ExperimentService experimentService;

    @Before
    public void before() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
//        assertThat(mockingDetails(claims).isMock()).isTrue();
//        assertThat(mockingDetails(securityContext).isMock()).isTrue();
//        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(experimentService).isMock()).isTrue();
//        SecurityContextHolder.setContext(securityContext);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testDeleteExperimentBadAuthenticationPrincipalType() throws Exception {
        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String teamId = RandomStringUtils.randomAlphabetic(8);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("");

        mockMvc.perform(delete("/experiments/" + experimentId + "/teams/" + teamId))
                .andExpect(status().isForbidden());
    }
}
