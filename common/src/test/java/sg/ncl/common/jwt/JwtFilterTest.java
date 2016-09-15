package sg.ncl.common.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.common.authentication.Role;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Te Ye
 * @version 1.0
 */
public class JwtFilterTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    private Key apiKey;
    private JwtFilter jwtFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Before
    public void before() {
        apiKey = new SecretKeySpec("123".getBytes(), SignatureAlgorithm.HS256.getJcaName());
        jwtFilter = new JwtFilter(apiKey, authenticationManager);
    }

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
    public void testPostRegistrationSlash() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/registrations/");
        when(request.getMethod()).thenReturn("POST");
        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testPostRegistration() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/registrations");
        when(request.getMethod()).thenReturn("POST");
        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testIsUserWhitelistEmailVerify() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/users/abc/emails/abc");
        when(request.getMethod()).thenReturn("PUT");

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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
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
            Assert.fail();
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Missing or invalid Authorization header."));
        }
    }

    @Test
    public void testGetTeamsBadObject() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        final String jwt = Jwts.builder()
                .setSubject("john")
                .setIssuer("Authentication")
                .claim("roles", "rubbish")
                // sign the JWT with the given algorithm and apiKey
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        try {
            jwtFilter.doFilter(request, response, filterChain);
            Assert.fail();
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Bad object."));
        }
    }

    @Test
    public void testGetTeamsInvalidRoles() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        final String jwt = Jwts.builder()
                .setSubject("john")
                .setIssuer("Authentication")
                .claim("roles", Arrays.asList(""))
                // sign the JWT with the given algorithm and apiKey
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        try {
            jwtFilter.doFilter(request, response, filterChain);
            Assert.fail();
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Invalid roles."));
        }
    }

    @Test
    public void testGetTeamsValidRoles() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        final String jwt = Jwts.builder()
                .setSubject("john")
                .setIssuer("Authentication")
                .claim("roles", Arrays.asList(Role.USER))
                // sign the JWT with the given algorithm and apiKey
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        jwtFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void testMalformedJwtException() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + "abc");

        try {
            jwtFilter.doFilter(request, response, filterChain);
            Assert.fail();
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Invalid token."));
        }
    }

    @Test
    public void testSignatureException() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        RememberMeAuthenticationToken rememberMeAuthenticationToken = mock(RememberMeAuthenticationToken.class);

        final String jwt = Jwts.builder()
                .setSubject("john")
                .setIssuer("Authentication")
                .claim("roles", Arrays.asList(Role.USER))
                // sign the JWT with the given algorithm and apiKey
                .signWith(SignatureAlgorithm.HS256, new SecretKeySpec("456".getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                .compact();

        when(request.getRequestURI()).thenReturn("/teams/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(authenticationManager.authenticate(rememberMeAuthenticationToken)).thenReturn(rememberMeAuthenticationToken);

        try {
            jwtFilter.doFilter(request, response, filterChain);
            Assert.fail();
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Invalid token."));
        }
    }
}
