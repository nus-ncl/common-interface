package sg.ncl.service.authentication.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.service.authentication.AbstractTest;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.NeitherUsernameNorPasswordModifiedException;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameNullOrEmptyException;
import sg.ncl.service.authentication.web.CredentialsInfo;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;

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

    @Mock
    private AdapterDeterlab adapterDeterlab;

    @Before
    public void before() {
        assertThat(mockingDetails(passwordEncoder).isMock(), is(true));
        assertThat(mockingDetails(credentialsRepository).isMock(), is(true));
        credentialsService = new CredentialsServiceImpl(credentialsRepository, passwordEncoder, adapterDeterlab);
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndPassword() {
        final Credentials credentialsInfo = new CredentialsInfo("id", "username", "password", null);

        when(passwordEncoder.encode(anyString())).thenReturn(credentialsInfo.getPassword());
        when(credentialsRepository.save(any(CredentialsEntity.class))).thenAnswer(i -> i.getArgumentAt(0, CredentialsEntity.class));

        final Credentials credentials = credentialsService.addCredentials(credentialsInfo);

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(credentials.getId(), is(equalTo(credentialsInfo.getId())));
        assertThat(credentials.getUsername(), is(equalTo(credentialsInfo.getUsername())));
        assertThat(credentials.getPassword(), is(equalTo(credentialsInfo.getPassword())));
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", null, null);

        exception.expect(PasswordNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", "", null);

        exception.expect(PasswordNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsGoodIdAndNullUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", null, "password", null);

        exception.expect(UsernameNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsGoodIdAndEmptyUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "", "password", null);

        exception.expect(UsernameNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "password", null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "username", "password", null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, "", null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", "", null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, "", null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", "", null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsDuplicateUsername() {
        final CredentialsEntity credentials = getCredentialsEntity();

        when(credentialsRepository.findByUsername(credentials.getUsername())).thenReturn(credentials);

        exception.expect(UsernameAlreadyExistsException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testAddCredentialsDuplicateUserId() {
        final CredentialsEntity credentials = getCredentialsEntity();

        when(credentialsRepository.findOne(credentials.getId())).thenReturn(credentials);

        exception.expect(UserIdAlreadyExistsException.class);

        credentialsService.addCredentials(credentials);
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
        final String username = "username";
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(null, username, password, null);

        when(credentialsRepository.findOne(anyString())).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn(password);
        when(credentialsRepository.save(any(CredentialsEntity.class))).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndNullPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
        final String username = "username";
        final String password = entity.getPassword();
        final CredentialsInfo info = new CredentialsInfo(null, username, null, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(credentialsRepository.save(entity)).thenReturn(entity);

        credentialsService.updateCredentials(entity.getId(), info);

        verify(passwordEncoder, times(0)).encode(anyString());
        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername(), is(equalTo(username)));
        assertThat(entity.getPassword(), is(equalTo(password)));
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndEmptyPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
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
        final CredentialsEntity entity = getCredentialsEntity();
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
        final CredentialsEntity entity = getCredentialsEntity();
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
