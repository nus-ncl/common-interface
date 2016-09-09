package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static sg.ncl.common.jwt.JwtAutoConfiguration.*;

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

}
