package sg.ncl.service.authentication.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.Util;
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

import javax.inject.Inject;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Inject
    private CredentialsService credentialsService;

//    @Before
//    public void before() {
//        credentialsService = new CredentialsService(credentialsRepository, passwordEncoder);
//    }

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
        when(credentialsRepository.save(Mockito.any(CredentialsEntity.class))).thenAnswer(invocation -> invocation.getArgumentAt(0, CredentialsEntity.class));

        final CredentialsEntity entity = credentialsService.addCredentials(credentials);

        verify(credentialsRepository, times(1)).save(Mockito.any(CredentialsEntity.class));
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
    public void testGoodUpdateCredentials() {
        final CredentialsEntity entity = Util.getCredentialsEntity();
        final String username = "username";
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(entity.getId(), username, password, null);

        when(credentialsRepository.findByUsername(entity.getUsername())).thenAnswer(invocation -> entity);
        when(passwordEncoder.encode(entity.getPassword())).thenAnswer(invocation -> invocation.getArgumentAt(0, String.class));
        when(credentialsRepository.save(entity)).thenAnswer(invocation -> invocation.getArgumentAt(0, CredentialsEntity.class));

        credentialsService.updateCredentials(entity.getId(), info);

        verify(credentialsRepository, times(1)).save(Mockito.any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndPassword() {
        final Credentials credentials = new CredentialsInfo(null, null, null, null);

        exception.expect(NullUsernameException.class);

        credentialsService.updateCredentials("id", credentials);
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndEmptyPassword() {
        final Credentials credentials = new CredentialsInfo(null, null, "", null);

        exception.expect(NullPasswordException.class);

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
