package sg.ncl.service.authentication.logic;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.domain.AuthenticationService;
import sg.ncl.service.authentication.domain.Authorization;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.InvalidCredentialsException;
import sg.ncl.service.user.data.jpa.UserEntity;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;
import static sg.ncl.service.authentication.util.TestUtil.getUserEntity;

/**
 * @author Christopher Zhong
 */
@ActiveProfiles({"mock-password-encoder", "mock-credentials-repository"})
public class AuthenticationServiceTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    //    @Inject
    @Mock
    private CredentialsRepository credentialsRepository;
    //    @Inject
    @Mock
    private PasswordEncoder passwordEncoder;
    @Inject
    private SignatureAlgorithm signatureAlgorithm;
    @Inject
    private Key apiKey;
    @Inject
    private Duration expiryDuration;
    //    @Inject
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;

    @Before
    public void before() {
        assertThat(mockingDetails(credentialsRepository).isMock(), is(true));
        assertThat(mockingDetails(passwordEncoder).isMock(), is(true));
        authenticationService = new AuthenticationServiceImpl(credentialsRepository, passwordEncoder, signatureAlgorithm, apiKey, expiryDuration, userService);
    }

    @Test
    public void testAuthenticationServiceExists() {
        assertThat(authenticationService, is(not(nullValue(AuthenticationService.class))));
    }

    @Test
    public void testLoginGood() {
        final CredentialsEntity entity = getCredentialsEntity();
        final UserEntity user = getUserEntity();

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(entity);
        when(passwordEncoder.matches(eq(entity.getPassword()), eq(entity.getPassword()))).thenReturn(true);
        when(userService.getUser(entity.getId())).thenReturn(user);

        final Authorization authorization = authenticationService.login(entity.getUsername(), entity.getPassword());

        assertThat(authorization.getId(), is(equalTo(entity.getId())));
        final JwtParser parser = Jwts.parser()
                .setSigningKey(apiKey)
                .requireSubject(entity.getId())
                .requireIssuer(AuthenticationService.class.getName());
        final Claims body = parser.parseClaimsJws(authorization.getToken()).getBody();
        assertThat(body.getIssuedAt(), is(not(nullValue(Date.class))));
        assertThat(body.getExpiration(), is(not(nullValue(Date.class))));
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
        final UserEntity user = getUserEntity();

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(entity);
        when(passwordEncoder.matches(eq(entity.getPassword()), anyString())).thenReturn(false);
        when(userService.getUser(entity.getId())).thenReturn(user);

        exception.expect(InvalidCredentialsException.class);
        exception.expectMessage(entity.getUsername());

        authenticationService.login(entity.getUsername(), entity.getPassword());
    }

}
