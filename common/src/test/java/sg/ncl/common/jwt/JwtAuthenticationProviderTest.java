package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import sg.ncl.common.jwt.JwtTokenTest.RolesImpl;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

/**
 * @author Te Ye
 */
public class JwtAuthenticationProviderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private JwtParser jwtParser;
    @Mock
    private Authentication authentication;
    @Mock
    private Jws<Claims> jws;
    @Mock
    private Claims claims;

    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Before
    public void before() {
        jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtParser);
    }

    @Test
    public void testAuthenticate() throws Exception {
        final String token = "token";
        doReturn(token).when(authentication).getCredentials();
        doReturn(jws).when(jwtParser).parseClaimsJws(anyString());
        doReturn(claims).when(jws).getBody();
        final ZonedDateTime now = ZonedDateTime.now();
        doReturn(Date.from(now.minusDays(1).toInstant())).when(claims).getIssuedAt();
        doReturn(Date.from(now.plusDays(1).toInstant())).when(claims).getExpiration();
        doReturn(Date.from(now.minusDays(1).toInstant())).when(claims).getNotBefore();
        final Roles roles = new RolesImpl(Arrays.asList());
        doReturn(roles).when(claims).get(Roles.KEY, Roles.class);

        final Authentication authenticate = jwtAuthenticationProvider.authenticate(authentication);

        assertThat(authenticate).isInstanceOf(JwtToken.class);
        assertThat(authenticate.getCredentials()).isEqualTo(token);
        assertThat(authenticate.getPrincipal()).isEqualTo(claims);
        assertThat(authenticate.getAuthorities()).isEmpty();
        assertThat(authenticate.getDetails()).isEqualTo(claims);
        assertThat(authenticate.isAuthenticated()).isTrue();
    }

    @Test
    public void testAuthenticateExpiredException() throws Exception {
        final String token = "token";
        doReturn(token).when(authentication).getCredentials();
        doThrow(new ExpiredJwtException(null, null, null)).when(jwtParser).parseClaimsJws(anyString());

        exception.expect(BadCredentialsException.class);
        exception.expectCause(is(instanceOf(ExpiredJwtException.class)));

        jwtAuthenticationProvider.authenticate(authentication);
    }

    @Test
    public void testAuthenticateUnsupportedJwtException() throws Exception {
        final String token = "token";
        doReturn(token).when(authentication).getCredentials();
        doThrow(new UnsupportedJwtException(null)).when(jwtParser).parseClaimsJws(anyString());

        exception.expect(BadCredentialsException.class);
        exception.expectCause(is(instanceOf(UnsupportedJwtException.class)));

        jwtAuthenticationProvider.authenticate(authentication);
    }

    @Test
    public void testAuthenticateMalformedJwtException() throws Exception {
        final String token = "token";
        doReturn(token).when(authentication).getCredentials();
        doThrow(new MalformedJwtException(null)).when(jwtParser).parseClaimsJws(anyString());

        exception.expect(BadCredentialsException.class);
        exception.expectCause(is(instanceOf(MalformedJwtException.class)));

        jwtAuthenticationProvider.authenticate(authentication);
    }

    @Test
    public void testAuthenticateSignatureException() throws Exception {
        final String token = "token";
        doReturn(token).when(authentication).getCredentials();
        doThrow(new SignatureException(null)).when(jwtParser).parseClaimsJws(anyString());

        exception.expect(BadCredentialsException.class);
        exception.expectCause(is(instanceOf(SignatureException.class)));

        jwtAuthenticationProvider.authenticate(authentication);
    }

    @Test
    public void testAuthenticatePrematureJwtException() throws Exception {
        final String token = "token";
        doReturn(token).when(authentication).getCredentials();
        doThrow(new PrematureJwtException(null, null, null)).when(jwtParser).parseClaimsJws(anyString());

        exception.expect(BadCredentialsException.class);
        exception.expectCause(is(instanceOf(PrematureJwtException.class)));

        jwtAuthenticationProvider.authenticate(authentication);
    }

    @Test
    public void testSupports() throws Exception {
        assertThat(jwtAuthenticationProvider.supports(JwtToken.class)).isTrue();
    }
}
