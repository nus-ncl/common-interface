package sg.ncl.service.experiment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.authentication.AuthenticationAutoConfiguration;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.common.jwt.JwtAutoConfiguration;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.experiment.Util;
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
@WebMvcTest(controllers = ExperimentsController.class, secure = true)
@ContextConfiguration(classes = {ExperimentsController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class, JwtAutoConfiguration.class, AuthenticationAutoConfiguration.class})
public class ExperimentsControllerTest3 {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Inject
    private ObjectMapper mapper;
    @Inject
    private MockMvc mockMvc;

//    @Mock
//    private Claims claims;
//    @Mock
//    private Authentication authentication;
//    @Mock
//    private SecurityContext securityContext;

    @MockBean
    private ExperimentService experimentService;

    @Before
    public void before() {
//        assertThat(mockingDetails(claims).isMock()).isTrue();
//        assertThat(mockingDetails(securityContext).isMock()).isTrue();
//        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(experimentService).isMock()).isTrue();
    }

    @Test
    public void testDeleteExperimentGoodAuthenticationPrincipal() throws Exception {
        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String teamId = RandomStringUtils.randomAlphabetic(8);
        final Claims claims = mock(Claims.class);
        final String username = "ncl@nus.edu.sg";
        final String password = "deterinavm";
        final String s = Base64Utils.encodeToString((username + ":" + password).getBytes());
        final String bearer = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjZTE3OWUxOC1lMzFmLTRjMTEtYWUxOS03OGJlMTdhMTM4YWIiLCJpc3MiOiJzZy5uY2wuc2VydmljZS5hdXRoZW50aWNhdGlvbi5kb21haW4uQXV0aGVudGljYXRpb25TZXJ2aWNlIiwiaWF0IjoxNDc1NTYxOTU0LCJuYmYiOjE0NzU1NjE5NTQsImV4cCI6MTQ3NTU2NTU1NCwicm9sZXMiOlsiVVNFUiJdfQ.5VfIQ9u13MkCRQqL1R-aZedbubOC91Vt9s3O4CdUlxkeIJRkQ69N6idSBYaEv0rmzApUNA_QQEgUFbHPGQjrmw";

//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(authentication.getPrincipal()).thenReturn("999999999");
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(experimentService.deleteExperiment(eq(experimentId), eq(teamId), any(Claims.class))).thenReturn(Util.getExperimentsEntity());

        mockMvc.perform(delete("/experiments/" + experimentId + "/teams/" + teamId).header(HttpHeaders.AUTHORIZATION, "Bearer " + bearer))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
