package sg.ncl.common.jwt;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Te Ye
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JwtFilterTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Inject
    private JwtFilter jwtFilter;

    // -----------------------------------
    // ----- WHITELISTED ENDPOINTS -------
    // -----------------------------------
    // If tests passed should not have exception
    @Test
    public void testGetPublicTeamVisibility() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String[] param = {"PUBLIC"};

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getParameterValues("visibility")).thenReturn(param);
        when(request.getMethod()).thenReturn("GET");
        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testGetTeamName() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String[] param = {RandomStringUtils.randomAlphabetic(8)};

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getParameterValues("name")).thenReturn(param);
        when(request.getMethod()).thenReturn("GET");
        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testGetUsers() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/users/");
        when(request.getMethod()).thenReturn("GET");
        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testGetAuthentication() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/authentication/");
        when(request.getMethod()).thenReturn("GET");
        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testPostRegistration() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/registrations/");
        when(request.getMethod()).thenReturn("POST");
        jwtFilter.doFilter(request, response, filterChain);
    }

    // -----------------------------------
    // ----- BLACKLISTED ENDPOINTS -------
    // -----------------------------------
    @Test
    public void testGetExperiment() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/experiments/experiments");
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetExperimentUsers() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/experiments/users/" + RandomStringUtils.randomAlphabetic(20));
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetExperimentTeams() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/experiments/teams/" + RandomStringUtils.randomAlphabetic(20));
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostAddExperiment() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/experiments");
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testDeleteExperiment() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String expId = RandomStringUtils.randomAlphanumeric(20);
        String teamName = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/experiments/" + expId + "/teams/" + teamName);
        when(request.getMethod()).thenReturn("DELETE");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetRealization() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/realizations/" + id);
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetRealizationByExperimentId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String expId = RandomStringUtils.randomAlphanumeric(20);
        String teamName = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/realizations/team" + teamName + "/experiment/" + expId);
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostStartExperiment() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String expId = RandomStringUtils.randomAlphanumeric(20);
        String teamName = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/realizations/start/team/" + teamName + "/experiment/" + expId);
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostStopExperiment() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String expId = RandomStringUtils.randomAlphanumeric(20);
        String teamName = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/realizations/stop/team/" + teamName + "/experiment/" + expId);
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostRegisterRequestToApplyTeam() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/registrations/newTeam/" + id);
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostRegisterRequestToJoinTeam() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/registrations/joinApplications");
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostApproveJoinRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);
        String userId = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/registrations/teams/" + id + "/members/" + userId);
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testDeleteApproveJoinRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);
        String userId = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/registrations/teams/" + id + "/members/" + userId);
        when(request.getMethod()).thenReturn("DELETE");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostApproveRejectJoinTeam() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);
        String userId = RandomStringUtils.randomAlphabetic(8);

        when(request.getRequestURI()).thenReturn("/registrations/teams/" + id + "/owner/" + userId + "/?status=APPROVE");
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetDeterUID() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/registrations/user/" + id);
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostCreateTeam() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetAllTeams() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetTeamById() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/teams/" + id);
        when(request.getMethod()).thenReturn("GET");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPutUpdateTeam() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/teams/" + id);
        when(request.getMethod()).thenReturn("PUT");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostAddTeamMember() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/teams/" + id + "/members");
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPutUpdateUsers() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/users/" + id);
        when(request.getMethod()).thenReturn("PUT");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testPostUsersAddTeam() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(request.getRequestURI()).thenReturn("/users/" + id + "/teams");
        when(request.getMethod()).thenReturn("POST");

        try {
            jwtFilter.doFilter(request, response, filterChain);
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

}
