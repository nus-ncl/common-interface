package sg.ncl.service.authentication.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import static org.mockito.Mockito.when;

/**
 * @author Christopher Zhong
 */
public class AuthenticationServiceTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CredentialsRepository credentialsRepository;

    @Inject
    private SignatureAlgorithm signatureAlgorithm;
    @Inject
    private Key key;
    @Inject
    private Duration duration;

    private AuthenticationService authenticationService;

    @Before
    public void before() {
        authenticationService = new AuthenticationServiceImpl(credentialsRepository, passwordEncoder, signatureAlgorithm, key, duration);
    }

    @Test
    public void testLoginGood() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final CredentialsEntity credentialsEntity = new CredentialsEntity();

        credentialsEntity.setId(username);
        credentialsEntity.setUsername(username);
        credentialsEntity.setPassword(password);

        when(credentialsRepository.findByUsername(eq(username))).thenReturn(credentialsEntity);
        when(passwordEncoder.matches(eq(password), eq(password))).thenReturn(true);

        final String jwt = authenticationService.login(username, password);

        final JwtParser parser = Jwts.parser()
                .setSigningKey(key)
                .requireSubject(username)
                .requireIssuer(AuthenticationService.class.getName());
        final Claims body = parser.parseClaimsJws(jwt).getBody();
        assertThat(body.getIssuedAt(), is(not(nullValue(Date.class))));
        assertThat(body.getExpiration(), is(not(nullValue(Date.class))));
    }

    @Test
    public void testLoginCredentialsNotFound() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);

        when(credentialsRepository.findByUsername(eq(username))).thenReturn(null);

        exception.expect(CredentialsNotFoundException.class);
        exception.expectMessage(username);

        authenticationService.login(username, password);
    }

    @Test
    public void testLoginInvalidCredentials() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);

        when(credentialsRepository.findByUsername(eq(username))).thenReturn(new CredentialsEntity());
        when(passwordEncoder.matches(eq(password), anyString())).thenReturn(false);

        exception.expect(InvalidCredentialsException.class);
        exception.expectMessage(username);

        authenticationService.login(username, password);
    }

}
