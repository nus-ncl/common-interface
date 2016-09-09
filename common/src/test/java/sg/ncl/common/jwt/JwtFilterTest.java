package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
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

    // -----------------------------------
    // ----- BLACKLISTED ENDPOINTS -------
    // -----------------------------------
    @Test
    public void testExperimentPage() throws Exception {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(request.getRequestURI()).thenReturn("GET", "/experiments/experiments");
//        jwtFilter.doFilter(request, response, filterChain);
//        verify(response).sendRedirect("/aaaa.jsp");
    }

}
