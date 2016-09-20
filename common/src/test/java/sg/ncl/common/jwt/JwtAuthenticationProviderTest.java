package sg.ncl.common.jwt;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import sg.ncl.common.authentication.Role;

import javax.crypto.spec.SecretKeySpec;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;

/**
 * @author Te Ye
 */
public class JwtAuthenticationProviderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    private Key apiKey;
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Before
    public void before() {
        apiKey = new SecretKeySpec("123".getBytes(), SignatureAlgorithm.HS256.getJcaName());
        jwtAuthenticationProvider = new JwtAuthenticationProvider(apiKey);
    }

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
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();
        JwtToken jwtToken = new JwtToken(jwt);
        Authentication auth = jwtAuthenticationProvider.authenticate(jwtToken);

        Collection<? extends GrantedAuthority> result = auth.getAuthorities();

        assertThat(result).isEqualTo(rolesList);
    }

    @Test
    public void testExpiredJwtException() throws Exception {
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiry = now.minusHours(24L);

        Collection<GrantedAuthority> rolesList = new ArrayList<>();
        rolesList.add(Role.USER);

        final String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuer("Authentication")
                .setExpiration(Date.from(expiry.toInstant()))
                .setIssuedAt(Date.from(now.toInstant()))
                .setNotBefore(Date.from(now.toInstant()))
                .claim("roles", rolesList)
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        exception.expect(BadCredentialsException.class);
        exception.expectMessage(contains("JWT expired at " + expiry.toString()));
        exception.expectCause(is(instanceOf(ExpiredJwtException.class)));
        jwtAuthenticationProvider.authenticate(new JwtToken(jwt));
    }

    @Test
    public void UnsupportedJwtException() throws Exception {
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
                .compact();

        exception.expect(BadCredentialsException.class);
        exception.expectMessage("Unsigned Claims JWTs are not supported.");
        exception.expectCause(is(instanceOf(UnsupportedJwtException.class)));
        jwtAuthenticationProvider.authenticate(new JwtToken(jwt));
    }

    @Test
    public void MalformedJwtException() throws Exception {
        final String jwt = "abc";
        exception.expect(BadCredentialsException.class);
        exception.expectMessage("JWT strings must contain exactly 2 period characters. Found: 0");
        exception.expectCause(is(instanceOf(MalformedJwtException.class)));
        jwtAuthenticationProvider.authenticate(new JwtToken(jwt));
    }

    @Test
    public void SignatureException() throws Exception {
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
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        exception.expect(BadCredentialsException.class);
        exception.expectMessage("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
        exception.expectCause(is(instanceOf(SignatureException.class)));

        apiKey = new SecretKeySpec("456".getBytes(), SignatureAlgorithm.HS512.getJcaName());
        jwtAuthenticationProvider = new JwtAuthenticationProvider(apiKey);
        jwtAuthenticationProvider.authenticate(new JwtToken(jwt));
    }

    @Test
    public void IllegalArgumentException() throws Exception {
        exception.expect(BadCredentialsException.class);
        exception.expectMessage("JWT String argument cannot be null or empty.");
        exception.expectCause(is(instanceOf(IllegalArgumentException.class)));
        jwtAuthenticationProvider.authenticate(new JwtToken(null));
    }

    @Test
    public void testNotBefore() throws Exception {
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiry = now.plusHours(24L);
        final ZonedDateTime notBefore = now.plusHours(24L);

        Collection<GrantedAuthority> rolesList = new ArrayList<>();
        rolesList.add(Role.USER);

        final String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuer("Authentication")
                .setExpiration(Date.from(expiry.toInstant()))
                .setIssuedAt(Date.from(now.toInstant()))
                .setNotBefore(Date.from(notBefore.toInstant()))
                .claim("roles", rolesList)
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        exception.expect(BadCredentialsException.class);
        exception.expectMessage(contains("JWT must not be accepted before " + notBefore));
        exception.expectCause(is(instanceOf(PrematureJwtException.class)));
        jwtAuthenticationProvider.authenticate(new JwtToken(jwt));
    }

    @Test
    public void testClaimBeforeIssueDate() throws Exception {
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiry = now.plusHours(24L);

        Collection<GrantedAuthority> rolesList = new ArrayList<>();
        rolesList.add(Role.USER);

        final String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuer("Authentication")
                .setExpiration(Date.from(expiry.toInstant()))
                .setIssuedAt(Date.from(expiry.toInstant()))
                .setNotBefore(Date.from(now.toInstant()))
                .claim("roles", rolesList)
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        exception.expect(BadCredentialsException.class);
        exception.expectMessage("Claim is before issued date " + Date.from(expiry.toInstant()));
        jwtAuthenticationProvider.authenticate(new JwtToken(jwt));
    }

    @Test
    public void testSupports() throws Exception {
        assertThat(jwtAuthenticationProvider.supports(JwtToken.class), is(true));
    }
}
