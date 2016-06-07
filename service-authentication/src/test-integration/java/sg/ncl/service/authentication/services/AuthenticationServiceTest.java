package sg.ncl.service.authentication.services;

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
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.InvalidCredentialsException;

import javax.inject.Inject;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static sg.ncl.service.authentication.Util.getCredentialsEntity;

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

    @Before
    public void before() {
        assertThat(mockingDetails(credentialsRepository).isMock(), is(true));
        assertThat(mockingDetails(passwordEncoder).isMock(), is(true));
        authenticationService = new AuthenticationServiceImpl(credentialsRepository, passwordEncoder, signatureAlgorithm, apiKey, expiryDuration);
    }

    @Test
    public void testAuthenticationServiceExists() {
        assertThat(authenticationService, is(not(nullValue(AuthenticationService.class))));
    }

    @Test
    public void testLoginGood() {
        final CredentialsEntity entity = getCredentialsEntity();

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(entity);
        when(passwordEncoder.matches(eq(entity.getPassword()), eq(entity.getPassword()))).thenReturn(true);

        final String jwt = authenticationService.login(entity.getUsername(), entity.getPassword());

        final JwtParser parser = Jwts.parser()
                .setSigningKey(apiKey)
                .requireSubject(entity.getId())
                .requireIssuer(AuthenticationService.class.getName());
        final Claims body = parser.parseClaimsJws(jwt).getBody();
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

        when(credentialsRepository.findByUsername(eq(entity.getUsername()))).thenReturn(entity);
        when(passwordEncoder.matches(eq(entity.getPassword()), anyString())).thenReturn(false);

        exception.expect(InvalidCredentialsException.class);
        exception.expectMessage(entity.getUsername());

        authenticationService.login(entity.getUsername(), entity.getPassword());
    }

}
