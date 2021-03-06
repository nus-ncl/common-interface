package sg.ncl.service.authentication.logic;

import freemarker.template.Template;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.DomainProperties;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.data.jpa.PasswordResetRequestEntity;
import sg.ncl.service.authentication.data.jpa.PasswordResetRequestRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.NeitherUsernameNorPasswordModifiedException;
import sg.ncl.service.authentication.exceptions.PasswordResetRequestNotFoundException;
import sg.ncl.service.authentication.exceptions.PasswordResetRequestTimeoutException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameNullOrEmptyException;
import sg.ncl.service.authentication.web.CredentialsInfo;
import sg.ncl.service.mail.domain.MailService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sg.ncl.service.authentication.util.TestUtil.getCredentialsEntity;
import static sg.ncl.service.authentication.util.TestUtil.getPasswordResetRequestEntity;

/**
 * @author Christopher Zhong
 */
public class CredentialsServiceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private Claims claims;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CredentialsRepository credentialsRepository;
    @Mock
    private AdapterDeterLab adapterDeterLab;
    @Mock
    private MailService mailService;
    @Mock
    private DomainProperties domainProperties;
    @Mock
    private PasswordResetRequestRepository passwordResetRequestRepository;
    @Mock
    private Template newClassMemberResetPasswordTemplate;
    @Mock
    private Template passwordResetEmailTemplate;
    private CredentialsService credentialsService;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(passwordEncoder).isMock()).isTrue();
        assertThat(mockingDetails(credentialsRepository).isMock()).isTrue();

        credentialsService = new CredentialsServiceImpl(credentialsRepository, passwordEncoder, adapterDeterLab,
                mailService, domainProperties, passwordResetRequestRepository, passwordResetEmailTemplate, newClassMemberResetPasswordTemplate);
    }

    @Test
    public void testAddCredentialsGoodIdAndGoodUsernameAndGoodPassword() {
        final Credentials credentialsInfo = new CredentialsInfo("id", "username", "password", null, null);

        when(passwordEncoder.encode(anyString())).thenReturn(credentialsInfo.getPassword());
        when(credentialsRepository.save(any(CredentialsEntity.class))).thenAnswer(i -> i.getArgumentAt(0, CredentialsEntity.class));

        final Credentials credentials = credentialsService.addCredentials(credentialsInfo);

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(credentials.getId()).isEqualTo(credentialsInfo.getId());
        assertThat(credentials.getUsername()).isEqualTo(credentialsInfo.getUsername());
        assertThat(credentials.getPassword()).isEqualTo(credentialsInfo.getPassword());
        assertThat(credentials.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
        assertThat(credentials.getRoles()).hasSize(1).containsExactly(Role.USER);
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", null, null, null);

        exception.expect(PasswordNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsGoodIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "username", "", null, null);

        exception.expect(PasswordNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsGoodIdAndNullUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", null, "password", null, null);

        exception.expect(UsernameNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsGoodIdAndEmptyUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("id", "", "password", null, null);

        exception.expect(UsernameNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "username", "password", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndGoodUsernameAndGoodPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "username", "password", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, null, null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, null, "", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", null, null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsNullIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo(null, "", "", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, null, null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndNullUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", null, "", null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndNullPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", null, null, null);

        exception.expect(UserIdNullOrEmptyException.class);

        credentialsService.addCredentials(credentialsInfo);
    }

    @Test
    public void testAddCredentialsEmptyIdAndEmptyUsernameAndEmptyPassword() throws Exception {
        final CredentialsInfo credentialsInfo = new CredentialsInfo("", "", "", null, null);

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
        final CredentialsInfo info = new CredentialsInfo(null, username, password, null, null);

        when(credentialsRepository.findOne(anyString())).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn(password);
        when(credentialsRepository.save(any(CredentialsEntity.class))).thenReturn(entity);
        when(claims.getSubject()).thenReturn(entity.getId());

        credentialsService.updateCredentials(entity.getId(), info, claims);

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getPassword()).isEqualTo(password);
        assertThat(entity.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
        assertThat(entity.getRoles()).hasSize(1).containsExactly(Role.USER);
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndNullPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
        final String username = "username";
        final String password = entity.getPassword();
        final CredentialsInfo info = new CredentialsInfo(null, username, null, null, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(credentialsRepository.save(entity)).thenReturn(entity);
        when(claims.getSubject()).thenReturn(entity.getId());

        credentialsService.updateCredentials(entity.getId(), info, claims);

        verify(passwordEncoder, times(0)).encode(anyString());
        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getPassword()).isEqualTo(password);
        assertThat(entity.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
        assertThat(entity.getRoles()).hasSize(1).containsExactly(Role.USER);
    }

    @Test
    public void testUpdateCredentialsGoodUsernameAndEmptyPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
        final String username = "username";
        final String password = entity.getPassword();
        final CredentialsInfo info = new CredentialsInfo(null, username, "", null, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(credentialsRepository.save(entity)).thenReturn(entity);
        when(claims.getSubject()).thenReturn(entity.getId());

        credentialsService.updateCredentials(entity.getId(), info, claims);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getPassword()).isEqualTo(password);
        assertThat(entity.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
        assertThat(entity.getRoles()).hasSize(1).containsExactly(Role.USER);
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndGoodPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
        final String username = entity.getUsername();
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(null, null, password, null, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(passwordEncoder.encode(eq(password))).thenReturn(password);
        when(credentialsRepository.save(entity)).thenReturn(entity);
        when(claims.getSubject()).thenReturn(entity.getId());

        credentialsService.updateCredentials(entity.getId(), info, claims);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getPassword()).isEqualTo(password);
        assertThat(entity.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
        assertThat(entity.getRoles()).hasSize(1).containsExactly(Role.USER);
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndGoodPassword() {
        final CredentialsEntity entity = getCredentialsEntity();
        final String username = entity.getUsername();
        final String password = "password";
        final CredentialsInfo info = new CredentialsInfo(null, "", password, null, null);

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(passwordEncoder.encode(eq(password))).thenReturn(password);
        when(credentialsRepository.save(entity)).thenReturn(entity);
        when(claims.getSubject()).thenReturn(entity.getId());

        credentialsService.updateCredentials(entity.getId(), info, claims);

        verify(credentialsRepository, times(1)).save(any(CredentialsEntity.class));
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getPassword()).isEqualTo(password);
        assertThat(entity.getStatus()).isEqualTo(CredentialsStatus.ACTIVE);
        assertThat(entity.getRoles()).hasSize(1).containsExactly(Role.USER);
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndPassword() {
        final Credentials credentials = new CredentialsInfo(null, null, null, null, null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials(null, credentials, claims);
    }

    @Test
    public void testUpdateCredentialsNullUsernameAndEmptyPassword() {
        final Credentials credentials = new CredentialsInfo(null, null, "", null, null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials, claims);
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndNullPassword() {
        final Credentials credentials = new CredentialsInfo(null, "", null, null, null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials, claims);
    }

    @Test
    public void testUpdateCredentialsEmptyUsernameAndPassword() {
        final Credentials credentials = new CredentialsInfo(null, "", "", null, null);

        exception.expect(NeitherUsernameNorPasswordModifiedException.class);

        credentialsService.updateCredentials("id", credentials, claims);
    }

    @Test
    public void testUpdatePasswordCredentialsNotFound() {
        final Credentials credentials = new CredentialsInfo("id", "username", "password", null, null);

        when(credentialsRepository.findOne(credentials.getId())).thenReturn(null);
        when(claims.getSubject()).thenReturn("id");

        exception.expect(CredentialsNotFoundException.class);

        credentialsService.updateCredentials(credentials.getId(), credentials, claims);
    }

    @Test
    public void testUpdatePasswordCredentialsNotRightUser() {
        final CredentialsEntity entity = getCredentialsEntity();

        when(credentialsRepository.findOne(eq(entity.getId()))).thenReturn(entity);
        when(claims.getSubject()).thenReturn("id");

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Permission denied");

        credentialsService.updateCredentials(entity.getId(), entity , claims);
    }

    @Test
    public void testAddPasswordResetRequestNullUsername() {
        String jsonString = "{\"username\": \"\"}";

        exception.expect(UsernameNullOrEmptyException.class);

        credentialsService.addPasswordResetRequest(jsonString);
    }

    @Test
    public void testAddPasswordResetRequestUsernameNotExist() {
        String jsonString = "{\"username\": \"alice@nus.edu.sg\"}";

        exception.expect(CredentialsNotFoundException.class);

        credentialsService.addPasswordResetRequest(jsonString);
    }

    @Test
    public void testAddPasswordResetRequestGood() {
        String username = "alice";
        String jsonString = "{\"username\": " + "\"" + username +"\"}" ;
        CredentialsEntity credentials = new CredentialsEntity();

        when(credentialsRepository.findByUsername(username)).thenReturn(credentials);

        credentialsService.addPasswordResetRequest(jsonString);
    }

    @Test
    public void testResetPasswordUnknownId() {
        String key = RandomStringUtils.randomAlphanumeric(20);
        String password = "password";
        String jsonString = "{\"key\": " + "\"" + key +"\", \"new\": \"" + password + "\"}" ;

        exception.expect(PasswordResetRequestNotFoundException.class);

        credentialsService.resetPassword(jsonString);
    }

    @Test
    public void testResetPasswordRequestTimeout() {
        String key = RandomStringUtils.randomAlphanumeric(20);
        String password = "password";
        String jsonString = "{\"key\": " + "\"" + key +"\", \"new\": \"" + password + "\"}" ;
        PasswordResetRequestEntity passwordEntity = getPasswordResetRequestEntity();
        passwordEntity.setTime(passwordEntity.getTime().minusHours(73));

        when(passwordResetRequestRepository.findByHash(anyString())).thenReturn(passwordEntity);

        exception.expect(PasswordResetRequestTimeoutException.class);

        credentialsService.resetPassword(jsonString);
    }

    @Test
    public void testResetPasswordCredentialsNotFound() {
        String key = RandomStringUtils.randomAlphanumeric(20);
        String password = "password";
        String jsonString = "{\"key\": " + "\"" + key +"\", \"new\": \"" + password + "\"}" ;
        PasswordResetRequestEntity passwordEntity = getPasswordResetRequestEntity();

        when(passwordResetRequestRepository.findByHash(anyString())).thenReturn(passwordEntity);

        exception.expect(CredentialsNotFoundException.class);

        credentialsService.resetPassword(jsonString);
    }


    @Test
    public void testResetPasswordNullPassword() {
        String key = RandomStringUtils.randomAlphanumeric(20);
        String password = "";
        String jsonString = "{\"key\": " + "\"" + key +"\", \"new\": \"" + password + "\"}" ;
        PasswordResetRequestEntity passwordEntity = getPasswordResetRequestEntity();
        CredentialsEntity credentialsEntity = getCredentialsEntity();

        when(passwordResetRequestRepository.findByHash(anyString())).thenReturn(passwordEntity);
        when(credentialsRepository.findByUsername(anyString())).thenReturn(credentialsEntity);

        exception.expect(PasswordNullOrEmptyException.class);

        credentialsService.resetPassword(jsonString);
    }


    @Test
    public void testResetPasswordGood() {
        String key = RandomStringUtils.randomAlphanumeric(20);
        String password = "password";
        String jsonString = "{\"key\": " + "\"" + key +"\", \"new\": \"" + password + "\"}" ;
        PasswordResetRequestEntity passwordEntity = getPasswordResetRequestEntity();
        CredentialsEntity credentialsEntity = getCredentialsEntity();

        when(passwordResetRequestRepository.findByHash(anyString())).thenReturn(passwordEntity);
        when(credentialsRepository.findByUsername(anyString())).thenReturn(credentialsEntity);

        credentialsService.resetPassword(jsonString);
    }
}
