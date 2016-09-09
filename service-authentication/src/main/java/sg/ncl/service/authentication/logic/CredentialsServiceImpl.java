package sg.ncl.service.authentication.logic;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsService;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.service.authentication.validation.Validator.check;
import static sg.ncl.service.authentication.validation.Validator.checkId;
import static sg.ncl.service.authentication.validation.Validator.checkPassword;
import static sg.ncl.service.authentication.validation.Validator.checkRoles;
import static sg.ncl.service.authentication.validation.Validator.checkStatus;
import static sg.ncl.service.authentication.validation.Validator.checkUsername;
import static sg.ncl.service.authentication.validation.Validator.validateForUpdate;

/**
 * @author Christopher Zhong
 */
@Service
@Slf4j
public class CredentialsServiceImpl implements CredentialsService {

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdapterDeterLab adapterDeterLab;

    @Inject
    CredentialsServiceImpl(@NotNull final CredentialsRepository credentialsRepository, @NotNull final PasswordEncoder passwordEncoder, @NotNull final AdapterDeterLab adapterDeterLab) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.adapterDeterLab = adapterDeterLab;
    }

    @Transactional
    @Override
    public List<Credentials> getAll() {
        return credentialsRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Credentials addCredentials(@NotNull final Credentials credentials) {
        check(credentials);
        // check if the user id already exists
        if (credentialsRepository.findOne(credentials.getId()) == null) {
            // check if the username already exists
            if (credentialsRepository.findByUsername(credentials.getUsername()) == null) {
                final CredentialsEntity entity = new CredentialsEntity();
                entity.setId(credentials.getId());
                entity.setUsername(credentials.getUsername());
                hashPassword(entity, credentials.getPassword());
                entity.setStatus(CredentialsStatus.ACTIVE);
                credentials.getRoles().forEach(entity::addRole);
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
    @Deprecated
    @Override
    // FIXME break into two methods, updateUsername, and updatePassword
    public Credentials updateCredentials(@NotNull final String id, @NotNull final Credentials credentials) {
        validateForUpdate(credentials);
        final CredentialsEntity entity = findCredentials(credentials);
        if (credentials.getUsername() != null && !credentials.getUsername().isEmpty()) {
            entity.setUsername(credentials.getUsername());
        }
        if (credentials.getPassword() != null && !credentials.getPassword().isEmpty()) {
            hashPassword(entity, credentials.getPassword());
        }

        final CredentialsEntity savedEntity = credentialsRepository.save(entity);
        changePassword(id, credentials.getPassword());
        log.info("Credentials updated: {}", savedEntity);
        return savedEntity;
    }

    @Transactional
    @Override
    public Credentials updateUsername(@NotNull final Credentials credentials) {
        checkUsername(credentials);
        final CredentialsEntity entity = findCredentials(credentials);
        entity.setUsername(credentials.getUsername());
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Username updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials updatePassword(@NotNull final Credentials credentials) {
        checkPassword(credentials);
        final CredentialsEntity entity = findCredentials(credentials);
        hashPassword(entity, credentials.getPassword());
        changePassword(credentials.getId(), credentials.getPassword());
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Password updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials updateStatus(@NotNull final Credentials credentials) {
        checkStatus(credentials);
        final CredentialsEntity entity = findCredentials(credentials);
        entity.setStatus(credentials.getStatus());
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Status updated: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials addRoles(@NotNull final Credentials credentials) {
        checkRoles(credentials);
        final CredentialsEntity entity = findCredentials(credentials);
        credentials.getRoles().forEach(entity::addRole);
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Roles added: {}", saved);
        return saved;
    }

    @Transactional
    @Override
    public Credentials removeRoles(@NotNull final Credentials credentials) {
        checkRoles(credentials);
        final CredentialsEntity entity = findCredentials(credentials);
        credentials.getRoles().forEach(entity::removeRole);
        final CredentialsEntity saved = credentialsRepository.save(entity);
        log.info("Roles removed: {}", saved);
        return saved;
    }

    private CredentialsEntity findCredentials(final Credentials credentials) {
        checkId(credentials);
        final CredentialsEntity entity = credentialsRepository.findOne(credentials.getId());
        // check if the username exists
        if (entity == null) {
            log.warn("Credentials for '{}' not found", credentials.getId());
            throw new CredentialsNotFoundException(credentials.getId());
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

        log.info("Credentials to be updated on Deter: {}", adapterObject.toString());

        adapterDeterLab.updateCredentials(adapterObject.toString());
    }

}
