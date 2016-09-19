package sg.ncl.service.authentication.logic;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.common.jwt.JwtAutoConfiguration;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.domain.AuthenticationService;
import sg.ncl.service.authentication.domain.Authorization;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.InvalidCredentialsException;

import javax.inject.Inject;
import java.security.Key;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;

/**
 * @author Christopher Zhong
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JwtAutoConfiguration.class)
@TestPropertySource(properties = {
        "ncl.jwt.api-key=123",
        "ncl.jwt.expiry-duration=PT1H",
        "ncl.jwt.signing-algorithm=HS256"
})
public class AuthenticationServiceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private CredentialsRepository credentialsRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Inject
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.ES256;
    @Inject
    private Key apiKey;
    @Inject
    private Duration expiryDuration = Duration.ZERO;

    private AuthenticationService authenticationService;

    @Before
    public void before() {
        assertThat(mockingDetails(credentialsRepository).isMock()).isTrue();
        assertThat(mockingDetails(passwordEncoder).isMock()).isTrue();

        authenticationService = new AuthenticationServiceImpl(credentialsRepository, passwordEncoder, signatureAlgorithm, apiKey, expiryDuration);
    }

    @Test
    public void testLoginGood() {
        final CredentialsEntity entity = getCredentialsEntity();

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(entity);
        when(passwordEncoder.matches(eq(entity.getPassword()), eq(entity.getPassword()))).thenReturn(true);

        final Authorization authorization = authenticationService.login(entity.getUsername(), entity.getPassword());

        assertThat(authorization.getId()).isEqualTo(entity.getId());
        assertThat(authorization.getToken()).isNotNull();
        final JwtParser parser = Jwts.parser().setSigningKey(apiKey);
        final Claims body = parser.parseClaimsJws(authorization.getToken()).getBody();
        assertThat(body.getSubject()).isNotNull().isEqualTo(entity.getId());
        assertThat(body.getIssuer()).isNotNull().isEqualTo(AuthenticationService.class.getName());
        assertThat(body.getIssuedAt()).isNotNull();
        assertThat(body.getNotBefore()).isNotNull();
        assertThat(body.getExpiration()).isNotNull();
        assertThat(body.get("roles")).isNotNull();
        assertThat(body.get("roles").toString().trim()).isEqualTo(entity.getRoles().toString().trim());
    }

    @Test
    public void testLoginCredentialsNotFound() {
        final CredentialsEntity entity = getCredentialsEntity();

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(null);

        exception.expect(CredentialsNotFoundException.class);
        exception.expectMessage(entity.getUsername());

        authenticationService.login(entity.getUsername(), entity.getPassword());
    }

    @Test
    public void testLoginInvalidCredentials() {
        final CredentialsEntity entity = getCredentialsEntity();

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(entity);
        when(passwordEncoder.matches(eq(entity.getPassword()), anyString())).thenReturn(false);

        exception.expect(InvalidCredentialsException.class);
        exception.expectMessage(entity.getUsername());

        authenticationService.login(entity.getUsername(), entity.getPassword());
    }

}
