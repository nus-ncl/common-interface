package sg.ncl.service.authentication.services;

import org.apache.commons.lang3.RandomStringUtils;
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
import sg.ncl.service.authentication.Util;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.dtos.CredentialsInfo;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.NeitherUsernameNorPasswordModifiedException;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Christopher Zhong
 */
@ActiveProfiles({"mock-password-encoder", "mock-credentials-repository"})
public class CredentialsServiceTest extends AbstractTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    //    @Inject
    @Mock
    private PasswordEncoder passwordEncoder;
    //    @Inject
    @Mock
    private CredentialsRepository credentialsRepository;
    //    @Inject
    private CredentialsService credentialsService;

    @Before
    public void before() {
        assertThat(mockingDetails(passwordEncoder).isMock(), is(true));
        assertThat(mockingDetails(credentialsRepository).isMock(), is(true));
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
        final Credentials credentials = new CredentialsInfo(userId, username, password, null);

        when(passwordEncoder.encode(password)).thenAnswer(invocation -> invocation.getArgumentAt(0, String.class));
        when(credentialsRepository.save(any(CredentialsEntity.class))).thenAnswer(invocation -> invocation.getArgumentAt(0, CredentialsEntity.class));

        final CredentialsEntity entity = credentialsService.addCredentials(credentials);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
        assertThat(entity.getId(), is(equalTo(userId)));
    }

    @Test
    public void testAddCredentialsNullUsername() {
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(userId, null, password, null);

        exception.expect(NullUsernameException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsNullPassword() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String userId = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(userId, username, null, null);

        exception.expect(NullPasswordException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsNullUserId() {
        final String username = RandomStringUtils.randomAlphanumeric(20);
        final String password = RandomStringUtils.randomAlphanumeric(20);
        final Credentials credentials = new CredentialsInfo(null, username, password, null);

        exception.expect(NullUserIdException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsDuplicateUsername() {
        final CredentialsEntity credentials = Util.getCredentialsEntity();

        when(credentialsRepository.findByUsername(credentials.getUsername())).thenReturn(credentials);

        exception.expect(UsernameAlreadyExistsException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsDuplicateUserId() {
        final CredentialsEntity credentials = Util.getCredentialsEntity();

        when(credentialsRepository.findOne(credentials.getId())).thenReturn(credentials);

        exception.expect(UserIdAlreadyExistsException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndPassword() {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        final String username = "username";
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(null, username, password, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(passwordEncoder.encode(eq(password))).thenReturn(password);
        when(credentialsRepository.save(entity)).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndNullPassword() {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        final String username = "username";
        final String password = entity.getPassword();
        final CredentialsInfo info = new CredentialsInfo(null, username, null, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(credentialsRepository.save(entity)).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndEmptyPassword() {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        final String username = "username";
        final String password = entity.getPassword();
        final CredentialsInfo info = new CredentialsInfo(null, username, "", null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(credentialsRepository.save(entity)).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndGoodPassword() {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        final String username = entity.getUsername();
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(null, null, password, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(passwordEncoder.encode(eq(password))).thenReturn(password);
        when(credentialsRepository.save(entity)).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndGoodPassword() {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        final String username = entity.getUsername();
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(null, "", password, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(passwordEncoder.encode(eq(password))).thenReturn(password);
        when(credentialsRepository.save(entity)).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndPassword() {
        final Credentials credentials = new CredentialsInfo(null, null, null, null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials);
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndEmptyPassword() {
        final Credentials credentials = new CredentialsInfo(null, null, "", null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials);
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndNullPassword() {
        final Credentials credentials = new CredentialsInfo(null, "", null, null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials);
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndPassword() {
        final Credentials credentials = new CredentialsInfo(null, "", "", null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials);
    }

    @Test
    public void testUpdatePasswordCredentialsNotFound() {
        final Credentials credentials = new CredentialsInfo("id", "username", "password", null);

        when(credentialsRepository.findOne(credentials.getId())).thenReturn(null);

        exception.expect(CredentialsNotFoundException.class);

        credentialsService.updateCredentials(credentials.getId(), credentials);
    }

}
