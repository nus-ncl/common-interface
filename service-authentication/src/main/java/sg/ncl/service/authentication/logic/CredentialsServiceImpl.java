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
import sg.ncl.service.authentication.exceptions.*;
import sg.ncl.service.mail.domain.MailService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static sg.ncl.service.authentication.validation.Validator.*;

/**
 * @author Christopher Zhong
 */
@Service
@Slf4j
public class CredentialsServiceImpl implements CredentialsService {

    private static final int PASSWORD_RESET_REQUEST_TIMEOUT_HOUR = 72;

    private static final String TESTBED_OPS_EMAIL = "NCL Operations <testbed-ops@ncl.sg>";

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdapterDeterLab adapterDeterLab;
    private final MailService mailService;
    private final DomainProperties domainProperties;
    private final Template passwordResetEmailTemplate;
    private final Template studentResetPasswordTemplate;
    private final PasswordResetRequestRepository passwordResetRepository;

    @Inject
    CredentialsServiceImpl(
            @NotNull final CredentialsRepository credentialsRepository,
            @NotNull final PasswordEncoder passwordEncoder,
            @NotNull final AdapterDeterLab adapterDeterLab,
            @NotNull final MailService mailService,
            @NotNull final DomainProperties domainProperties,
            @NotNull final PasswordResetRequestRepository passwordResetRepository,
            @NotNull @Named("passwordResetEmailTemplate") final Template passwordResetEmailTemplate,
            @NotNull @Named("studentResetPasswordTemplate") final Template studentResetPasswordTemplate) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.adapterDeterLab = adapterDeterLab;
        this.mailService = mailService;
        this.domainProperties = domainProperties;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordResetEmailTemplate = passwordResetEmailTemplate;
        this.studentResetPasswordTemplate = studentResetPasswordTemplate;

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
                log.info("Credentials created for {}", saved.getId());
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
    public void removeCredentials(@NotNull final String id) {
        CredentialsEntity entity = findCredentials(id);
        Set<Role> roles = new HashSet<>(entity.getRoles());
        roles.forEach(entity::removeRole);
        credentialsRepository.save(entity);
        credentialsRepository.delete(entity);
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
     * Invokes the reset password on Deterlab
     *
     * @param nclUserId the ncl UUID
     * @param password  the new password
     */
    private void resetPassword(String nclUserId, String password) {
        JSONObject adapterObject = new JSONObject();
        // FIXME: need to handle error when getDeterUserIdByNclUserId() returns nothing
        final String uid = adapterDeterLab.getDeterUserIdByNclUserId(nclUserId);
        adapterObject.put("uid", uid);
        adapterObject.put("password", password);

        adapterDeterLab.resetPassword(adapterObject.toString());
        log.info("Password was reset for Deter user {}", uid);
    }

    /**
     *
     * @param jsonString {
     *                   "username": "abc@edf.com"
     *                   }
     */
    @Override
    public void addPasswordResetRequest(String jsonString){
        final String username = new JSONObject(jsonString).getString("username");
        if(null == username || username.trim().isEmpty()) {
            log.warn("Username null or empty in password reset request");
            throw new UsernameNullOrEmptyException();
        }

        final Credentials one = credentialsRepository.findByUsername(username);
        if(null == one) {
            log.warn("User {} not found in password reset request", username);
            throw new CredentialsNotFoundException(username);
        }

        String key = RandomStringUtils.randomAlphanumeric(20);
        PasswordResetRequestEntity passwordResetRequestEntity = new PasswordResetRequestEntity();
        passwordResetRequestEntity.setHash(generateShaHash(key));
        passwordResetRequestEntity.setTime(ZonedDateTime.now());
        passwordResetRequestEntity.setUsername(username);
        passwordResetRepository.save(passwordResetRequestEntity);
        log.info("Password reset request saved: {}", passwordResetRequestEntity.getId());

        sendPasswordResetEmail(username, key);
    }
    /**
     *
     * @param username the email address
     * @param key the random string before hash
     */
    private void sendPasswordResetEmail(String username, String key) {

        final Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("domain", domainProperties.getDomain());
        map.put("key", key);

        /*
         * If sending email fails, we catch the exceptions and log them,
         * rather than throw the exceptions. Hence, the email will not cause
         * the main application to fail. If users cannot receive emails after
         * a certain amount of time, they should send email to support@ncl.sg
         */
        try {
            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(
                    passwordResetEmailTemplate, map);
            mailService.send(TESTBED_OPS_EMAIL, username,
                    "Your Request To Reset Password", msgText, false, null, null);
            log.info("Password reset email sent: {}", msgText);
        } catch (IOException | TemplateException e) {
            log.warn("{}", e);
        }
    }

    /**
     * Verify whether the password reset request timeout or not
     *
     * @param key the random string before hash
     *
     * @return credentialsEntity for the user who requests to reset password
     */
    private CredentialsEntity verifyPasswordResetRequestTimeout(String key) {

        final String hashedId = generateShaHash(key);
        PasswordResetRequestEntity one = passwordResetRepository.findByHash(hashedId);
        if(null == one) {
            log.warn("Password reset request NOT found {}", key);
            throw new PasswordResetRequestNotFoundException(key);
        }

        // check whether the request has timed out or not
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(one.getTime().plusHours(PASSWORD_RESET_REQUEST_TIMEOUT_HOUR))) {
            log.warn("Password reset request timeout: request date {}, now {}", one.getTime(), now);
            throw new PasswordResetRequestTimeoutException("requested on " + one.getTime() + ", now " + now);
        }

        return credentialsRepository.findByUsername(one.getUsername());
    }

    /**
     * Reset password
     *
     * @param jsonString {
     *                   "key": "1234abcd5678efgh",
     *                   "new": "password"
     *                   }
     *
     * @return credentialsEntity after resetting password
     */
    @Override
    @Transactional
    public Credentials resetPassword(final String jsonString) {

        final JSONObject tmp = new JSONObject(jsonString);
        final String key = tmp.getString("key");
        final String newPassword = tmp.getString("new");

        CredentialsEntity one = verifyPasswordResetRequestTimeout(key);

        if(null == one) {
            log.warn("Credentials not found for password reset request {}", key);
            throw new CredentialsNotFoundException("Password reset request " + key);
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            hashPassword(one, newPassword);
            resetPassword(one.getId(), newPassword);
            final CredentialsEntity saved = credentialsRepository.save(one);
            log.info("Password was reset for user {}", one.getUsername());
            return saved;
        }

        log.warn("Password null or empty in password reset!");
        throw new PasswordNullOrEmptyException();
    }

    @Override
    public boolean verifyPassword(@NotNull final String id, @NotNull final String password) {
        CredentialsEntity entity = findCredentials(id);
        return passwordEncoder.matches(password, entity.getPassword());
    }

    private static String generateShaHash(String str) {
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * used when a project leader tries to add students into his project
     * a password reset request will be created for the student who is then informed via email
     *
     * @param userName email address used to login to the web portal
     * @param projectName name of the project/team in which the member is added
     */
    @Override
    @Transactional
    public void addPasswordResetRequestForStudent(String userName, String projectName){

        if (null == userName || userName.trim().isEmpty()) {
            log.warn("Username null or empty in password reset request");
            throw new UsernameNullOrEmptyException();
        }

        final Credentials one = credentialsRepository.findByUsername(userName);
        if (null == one) {
            log.warn("User {} not found in credentials database", userName);
            throw new CredentialsNotFoundException(userName);
        }

        String key = RandomStringUtils.randomAlphanumeric(20);

        PasswordResetRequestEntity passwordResetRequestEntity = new PasswordResetRequestEntity();
        passwordResetRequestEntity.setHash(generateShaHash(key));
        passwordResetRequestEntity.setTime(ZonedDateTime.now());
        passwordResetRequestEntity.setUsername(userName);
        passwordResetRepository.save(passwordResetRequestEntity);
        log.info("Password reset request saved: {}", passwordResetRequestEntity.getId());

        final String message = "You have been added to a NCL Project (Name: " + projectName + ").";
        sendPasswordResetEmailToStudent(one.getId(), key, userName, message);
    }

    private void sendPasswordResetEmailToStudent(String uid, String key, String email, String message) {
        final Map<String, String> map = new HashMap<>();
        map.put("member", email);
        map.put("message", message);
        map.put("domain", domainProperties.getDomain());
        map.put("key", key);
        map.put("uid", uid);

        try {
            String msgText = FreeMarkerTemplateUtils.processTemplateIntoString(studentResetPasswordTemplate, map);
            mailService.send(TESTBED_OPS_EMAIL, email, "Reset Password For New Student Member", msgText, false, null, null);
            //log.info("Password reset email sent: {}", msgText);
        } catch (IOException | TemplateException e) {
            log.warn("{}", e);
        }
    }

    /**
     * New student member needs to set/reset his password when he first login
     *
     * @param uid uuid of the new student member
     * @param key the randomly generated key for reset the password
     * @param password new password
     */
    @Override
    @Transactional
    public void changePasswordStudent(String uid, String key, String password){

        CredentialsEntity credentialFromUid = credentialsRepository.findById(uid);
        if(null ==  credentialFromUid) {
            log.warn("Student password reset: credentials from {} not found", uid);
            throw new CredentialsNotFoundException(uid);
        }

        CredentialsEntity credentialFromKey = verifyPasswordResetRequestTimeout(key);

        if (!credentialFromKey.getId().equals(uid)) {
            log.warn("Student password reset: uid {} and key {} do not match", uid, key);
            throw new PasswordResetRequestNotMatchException("Password reset request does not match with the user!");
        }

        if (password != null && !password.trim().isEmpty()) {
            hashPassword(credentialFromUid, password);
            credentialsRepository.save(credentialFromUid);
            log.info("Student password reset for {} is successful", credentialFromUid.getUsername());
        } else {
            log.warn("Student password reset for {}: Password null or empty!", credentialFromUid.getUsername() );
            throw new PasswordNullOrEmptyException();
        }
    }

    @Override
    public void resetKeyStudent(String uid) {

        CredentialsEntity credentialFromUid = credentialsRepository.findById(uid);
        if(null ==  credentialFromUid) {
            log.warn("Student password key reset: credential from {} not found", uid);
            throw new CredentialsNotFoundException(uid);
        }
        String username = credentialFromUid.getUsername();

        List<PasswordResetRequestEntity>  passwordResetRequestEntityList = passwordResetRepository.findByUsername(username);
        if(passwordResetRequestEntityList.isEmpty()) {
            log.warn("Student password key reset: password reset request for {} not found", uid);
            throw new PasswordResetRequestNotFoundException(uid);
        }

        String key = RandomStringUtils.randomAlphanumeric(20);
        PasswordResetRequestEntity passwordResetRequestEntity = new PasswordResetRequestEntity();
        passwordResetRequestEntity.setHash(generateShaHash(key));
        passwordResetRequestEntity.setTime(ZonedDateTime.now());
        passwordResetRequestEntity.setUsername(username);
        PasswordResetRequestEntity saved = passwordResetRepository.save(passwordResetRequestEntity);
        log.info("New password key was generated for {}: {}", uid, saved.getId());

        sendPasswordResetEmailToStudent(uid, key, username, "Your key has been reset!");
    }

}
