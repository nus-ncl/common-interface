package sg.ncl.service.authentication.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.dtos.CredentialsInfo;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.NullPasswordException;
import sg.ncl.service.authentication.exceptions.NullUserIdException;
import sg.ncl.service.authentication.exceptions.NullUsernameException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sg.ncl.service.authentication.data.jpa.entities.CredentialsEntityTest.getCredentialsEntity;

/**
 * @author Christopher Zhong
 */
public class CredentialsServiceTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CredentialsRepository credentialsRepository;

    private CredentialsService credentialsService;

    @Before
    public void before() {
        credentialsService = new CredentialsService(credentialsRepository, passwordEncoder);
    }

    @Test
    public void testCredentialsServiceExists() {
        assertThat(credentialsService, is(not(nullValue(CredentialsService.class))));
    }

    @Test
    public void testGoodAddCredentials() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, password, userId, null);

        when(passwordEncoder.encode(password)).thenAnswer(invocation -> invocation.getArgumentAt(0, String.class));
        when(credentialsRepository.save(Mockito.any(CredentialsEntity.class))).thenAnswer(invocation -> invocation.getArgumentAt(0, CredentialsEntity.class));

        final CredentialsEntity entity = credentialsService.addCredentials(credentials);

        verify(credentialsRepository, times(1)).save(Mockito.any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
        assertThat(entity.getUserId(), is(equalTo(userId)));
    }

    @Test
    public void testAddCredentialsNullUsername() {
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(null, password, userId, null);

        exception.expect(NullUsernameException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsNullPassword() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, null, userId, null);

        exception.expect(NullPasswordException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsNullUserId() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, password, null, null);

        exception.expect(NullUserIdException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsDuplicateUsername() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, password, userId, null);

        when(credentialsRepository.findByUsername(username)).thenReturn(getCredentialsEntity());

        exception.expect(UsernameAlreadyExistsException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsDuplicateUserId() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, password, userId, null);

        when(credentialsRepository.findByUserId(userId)).thenReturn(getCredentialsEntity());

        exception.expect(UserIdAlreadyExistsException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testGoodUpdatePassword() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, password, null, null);
        final CredentialsEntity entity = new CredentialsEntity();
        entity.setUsername(username);
        entity.setPassword("password");

        when(credentialsRepository.findByUsername(username)).thenAnswer(invocation -> entity);
        when(passwordEncoder.encode(password)).thenAnswer(invocation -> invocation.getArgumentAt(0, String.class));
        when(credentialsRepository.save(Mockito.any(CredentialsEntity.class))).thenAnswer(invocation -> invocation.getArgumentAt(0, CredentialsEntity.class));

        credentialsService.updatePassword(credentials);

        verify(credentialsRepository, times(1)).save(Mockito.any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdatePasswordNullUsername() {
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(null, password, null, null);

        exception.expect(NullUsernameException.class);

        credentialsService.updatePassword(credentials);
    }

    @Test
    public void testUpdatePasswordNullPassword() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, null, null, null);

        exception.expect(NullPasswordException.class);

        credentialsService.updatePassword(credentials);
    }

    @Test
    public void testUpdatePasswordCredentialsNotFound() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(username, password, null, null);

        when(credentialsRepository.findByUsername(username)).thenReturn(null);

        exception.expect(CredentialsNotFoundException.class);

        credentialsService.updatePassword(credentials);
    }

}
