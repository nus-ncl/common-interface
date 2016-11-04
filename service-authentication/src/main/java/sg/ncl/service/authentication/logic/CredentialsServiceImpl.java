package sg.ncl.service.authentication.logic;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.DomainProperties;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.data.jpa.PasswordResetRequestEntity;
import sg.ncl.service.authentication.data.jpa.PasswordResetRequestRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.PasswordResetRequestNotFoundException;
import sg.ncl.service.authentication.exceptions.PasswordResetRequestTimeoutException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;
import sg.ncl.service.mail.domain.MailService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.ncl.service.authentication.validation.Validator.addCheck;
import static sg.ncl.service.authentication.validation.Validator.updateCheck;
import static sg.ncl.service.authentication.validation.Validator.checkUsername;
import static sg.ncl.service.authentication.validation.Validator.checkPassword;
import static sg.ncl.service.authentication.validation.Validator.checkStatus;
import static sg.ncl.service.authentication.validation.Validator.checkRoles;

/**
 * @author Christopher Zhong
 */
@Service
@Slf4j
public class CredentialsServiceImpl implements CredentialsService {

    private static final int PASSWORD_RESET_REQUEST_TIMEOUT_HOUR = 72;

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdapterDeterLab adapterDeterLab;
    private final MailService mailService;
    private final DomainProperties domainProperties;
    private final Template passwordResetEmailTemplate;
    private final PasswordResetRequestRepository passwordResetRepository;

    @Inject
    CredentialsServiceImpl(
            @NotNull final CredentialsRepository credentialsRepository,
            @NotNull final PasswordEncoder passwordEncoder,
            @NotNull final AdapterDeterLab adapterDeterLab,
            @NotNull final MailService mailService,
            @NotNull final DomainProperties domainProperties,
            @NotNull final PasswordResetRequestRepository passwordResetRepository,
            @NotNull @Named("passwordResetEmailTemplate") final Template passwordResetEmailTemplate) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.adapterDeterLab = adapterDeterLab;
        this.mailService = mailService;
        this.domainProperties = domainProperties;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordResetEmailTemplate = passwordResetEmailTemplate;
    }

    @Transactional
    @Override
    public List<Credentials> getAll() {
        return credentialsRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Credentials addCredentials(@NotNull final Credentials credentials) {
        addCheck(credentials);
        // check if the user id already exists
        if (credentialsRepository.findOne(credentials.getId()) == null) {
            // check if the username already exists
            if (credentialsRepository.findByUsername(credentials.getUsername()) == null) {
                final CredentialsEntity entity = new CredentialsEntity();
                entity.setId(credentials.getId());
                entity.setUsername(credentials.getUsername());
                hashPassword(entity, credentials.getPassword());
                entity.setStatus(CredentialsStatus.ACTIVE);
                entity.addRole(Role.USER);
                final CredentialsEntity saved = credentialsRepository.save(entity);
                log.info("Credentials created: {}", saved);
                return saved;
            }
            log.warn("Username '{}' is already associated with a credentials", credentials.getUsername());
            throw new UsernameAlreadyExistsException(credentials.getUsername());
        }
        log.warn("User Id '{}' is already associated with a credentials", credentials.getId());
        throw new UserIdAlreadyExistsException(credentials.getId());
    }

    @Transactional
    @Override
    public Credentials updateCredentials(@NotNull final String id, @NotNull final Credentials credentials, @NotNull final Claims claims) {
        updateCheck(id, credentials, claims);
        final CredentialsEntity entity = findCredentials(id);
        if (credentials.getUsername() != null && !credentials.getUsername().isEmpty()) {
            entity.setUsername(credentials.getUsername());
        }
        if (credentials.getPassword() != null && !credentials.getPassword().isEmpty()) {
            hashPassword(entity, credentials.getPassword());
            changePassword(id, credentials.getPassword());
        }
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Credentials updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials updateUsername(@NotNull final String id, @NotNull final Credentials credentials) {
        checkUsername(credentials);
        final CredentialsEntity entity = findCredentials(id);
        entity.setUsername(credentials.getUsername());
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Username updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials updatePassword(@NotNull final String id, @NotNull final Credentials credentials) {
        checkPassword(credentials);
        final CredentialsEntity entity = findCredentials(id);
        hashPassword(entity, credentials.getPassword());
        changePassword(credentials.getId(), credentials.getPassword());
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Password updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials updateStatus(@NotNull final String id, @NotNull final Credentials credentials) {
        checkStatus(credentials);
        final CredentialsEntity entity = findCredentials(id);
        entity.setStatus(credentials.getStatus());
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Status updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials addRoles(@NotNull final String id, @NotNull final Credentials credentials) {
        checkRoles(credentials);
        final CredentialsEntity entity = findCredentials(id);
        credentials.getRoles().forEach(entity::addRole);
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Roles added: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials removeRoles(@NotNull final String id, @NotNull final Credentials credentials) {
        checkRoles(credentials);
        final CredentialsEntity entity = findCredentials(id);
        credentials.getRoles().forEach(entity::removeRole);
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Roles removed: {}", saved);
        return saved;
    }

    private CredentialsEntity findCredentials(final String id) {
        final CredentialsEntity entity = credentialsRepository.findOne(id);
        // check if the username exists
        if (entity == null) {
            log.warn("Credentials for '{}' not found", id);
            throw new CredentialsNotFoundException(id);
        }
        return entity;
    }

    private void hashPassword(final CredentialsEntity entity, final String password) {
        entity.setPassword(passwordEncoder.encode(password));
    }

    /**
     * Invokes the change password on Deterlab
     *
     * @param nclUserId the ncl UUID
     * @param password  the clear password
     */
    private void changePassword(String nclUserId, String password) {
        JSONObject adapterObject = new JSONObject();
        // FIXME: need to handle error when getDeterUserIdByNclUserId() returns nothing
        adapterObject.put("uid", adapterDeterLab.getDeterUserIdByNclUserId(nclUserId));
        adapterObject.put("password1", password);
        adapterObject.put("password2", password);

        log.info("Credentials to be updated on Deter on ncl user id: {}", nclUserId);

        adapterDeterLab.updateCredentials(adapterObject.toString());
    }

    /**
     *
     * @param username the username to reset, should be an email address
     */
    public void addPasswordResetRequest(String username){
        final Credentials one = credentialsRepository.findByUsername(username);
        if(null == one) {
            log.warn("User {} not found in password reset", username);
            throw new CredentialsNotFoundException(username);
        }

        String id = RandomStringUtils.randomAlphanumeric(20);
        PasswordResetRequestEntity passwordResetRequestEntity = new PasswordResetRequestEntity();
        passwordResetRequestEntity.setHash(generateShaHash(id));
        passwordResetRequestEntity.setTime(ZonedDateTime.now());
        passwordResetRequestEntity.setUsername(username);
        passwordResetRepository.save(passwordResetRequestEntity);
        log.info("Password reset request saved: {}", passwordResetRequestEntity.toString());

        sendPasswordResetEmail(username, id);
    }
    /**
     *
     * @param username the email address
     * @param id the random ID before hash
     */
    private void sendPasswordResetEmail(String username, String id) {

        final Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("domain", domainProperties.getDomain());
        map.put("id", id);

        /*
         * If sending email fails, we catch the exceptions and log them,
         * rather than throw the exceptions. Hence, the email will not cause
         * the main application to fail. If users cannot receive emails after
         * a certain amount of time, they should send email to support@ncl.sg
         */
        try {
            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(
                    passwordResetEmailTemplate, map);
            mailService.send("testbed-ops@ncl.sg", username,
                    "NCL Testbed: Your Reset Password Request", msgText, false, null, null);
            log.info("Password reset email sent: {}", msgText);
        } catch (IOException | TemplateException e) {
            log.warn("{}", e);
        }
    }

    /**
     * Verify whether the password reset request is timeout or not
     *
     * @param id the random string before hash
     */
    public void verifyPasswordResetRequestTimeout(String id) {

        String hashedId = generateShaHash(id);
        log.info("hashed id {}", hashedId);
        PasswordResetRequestEntity one = passwordResetRepository.findByHash(hashedId);
        if(null == one) {
            log.warn("Password reset request NOT found {}", id);
            throw new PasswordResetRequestNotFoundException(id);
        }

        // check whether the request has timed out or not
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(one.getTime().plusHours(PASSWORD_RESET_REQUEST_TIMEOUT_HOUR))) {
            log.warn("Password reset request timeout: request date {}, now {}", one.getTime(), now);
            throw new PasswordResetRequestTimeoutException();
        }

        // do nothing, this password reset request looks ok
    }

    /**
     * Reset password
     *
     * @param credentials the username and password to be updated
     * @return
     */
    public Credentials resetPassword(final Credentials credentials) {

        CredentialsEntity one = credentialsRepository.findByUsername(credentials.getUsername());
        if(null == one) {
            log.warn("Credentials not found: {}", credentials.getUsername());
            throw new CredentialsNotFoundException(credentials.getUsername());
        }

        if (credentials.getPassword() != null && !credentials.getPassword().trim().isEmpty()) {
            hashPassword(one, credentials.getPassword());
            changePassword(one.getId(), credentials.getPassword());
            final CredentialsEntity saved = credentialsRepository.save(one);
            log.info("Password was reset: {}", saved);
            return saved;
        }

        log.warn("Password null or empty!");
        throw new PasswordNullOrEmptyException();
    }

    private String generateShaHash(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.warn("Error in getting message digest method {}", e);
            return String.valueOf(str.hashCode());
        }
        md.update(str.getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
