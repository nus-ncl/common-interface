package sg.ncl.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import sg.ncl.common.authentication.Role;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import java.security.Key;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Te Ye
 */
public class JwtAuthenticationProviderTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    private Key apiKey;
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Before
    public void before() {
        apiKey = new SecretKeySpec("123".getBytes(), SignatureAlgorithm.HS256.getJcaName());
        jwtAuthenticationProvider = new JwtAuthenticationProvider(apiKey);
    }

//    @Test
//    public void testNotAuthenticated() throws Exception {
//        RememberMeAuthenticationToken token = mock(RememberMeAuthenticationToken.class);
//        when(token.isAuthenticated()).thenReturn(false);
//        Authentication resultToken = jwtAuthenticationProvider.authenticate(token);
//        assertThat(resultToken.isAuthenticated(), is(token.isAuthenticated()));
//    }

    @Test
    public void testGetPrincipal() throws Exception {

        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiry = now.plusHours(24L);

        final String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuer("Authentication")
                .setExpiration(Date.from(expiry.toInstant()))
                .setIssuedAt(Date.from(now.toInstant()))
                .setNotBefore(Date.from(now.toInstant()))
                .claim("roles", Arrays.asList(Role.USER))
                // sign the JWT with the given algorithm and apiKey
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();
        JwtToken jwtToken = new JwtToken(jwt);
        Authentication auth = jwtAuthenticationProvider.authenticate(jwtToken);
        assertThat(auth.getPrincipal(), is(equalTo(userId)));
    }

    @Test
    public void testGetAuthorities() throws Exception {

        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiry = now.plusHours(24L);

        Collection<GrantedAuthority> rolesList = new ArrayList<>();
        rolesList.add(Role.USER);

        final String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuer("Authentication")
                .setExpiration(Date.from(expiry.toInstant()))
                .setIssuedAt(Date.from(now.toInstant()))
                .setNotBefore(Date.from(now.toInstant()))
                .claim("roles", rolesList)
                // sign the JWT with the given algorithm and apiKey
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();
        JwtToken jwtToken = new JwtToken(jwt);
        Authentication auth = jwtAuthenticationProvider.authenticate(jwtToken);

        Collection<? extends GrantedAuthority> result = auth.getAuthorities();

        assertThat(result).isEqualTo(rolesList);
//        assertThat(auth.getAuthorities(), containsInAnyOrder(rolesList));
    }
}
