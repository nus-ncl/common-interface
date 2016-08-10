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

import static sg.ncl.service.authentication.validation.Validator.validateForCreation;
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
    protected CredentialsServiceImpl(@NotNull final CredentialsRepository credentialsRepository, @NotNull final PasswordEncoder passwordEncoder, @NotNull final AdapterDeterLab adapterDeterLab) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.adapterDeterLab = adapterDeterLab;
    }

    @Transactional
    public List<Credentials> getAll() {
        return credentialsRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    public Credentials addCredentials(@NotNull final Credentials credentials) {
        validateForCreation(credentials);
        // check if the user id already exists
        if (credentialsRepository.findOne(credentials.getId()) == null) {
            // check if the username already exists
            if (credentialsRepository.findByUsername(credentials.getUsername()) == null) {
                final CredentialsEntity entity = new CredentialsEntity();
                entity.setUsername(credentials.getUsername());
                hashPassword(entity, credentials.getPassword());
                entity.setId(credentials.getId());
                entity.setStatus(CredentialsStatus.ACTIVE);
                final CredentialsEntity savedEntity = credentialsRepository.save(entity);
                log.info("Credentials created: {}", savedEntity);
                return savedEntity;
            }
            log.warn("Username '{}' is already associated with a credentials", credentials.getUsername());
            throw new UsernameAlreadyExistsException(credentials.getUsername());
        }
        log.warn("User Id '{}' is already associated with a credentials", credentials.getId());
        throw new UserIdAlreadyExistsException(credentials.getId());
    }

    @Transactional
    public Credentials updateCredentials(@NotNull final String id, @NotNull final Credentials credentials) {
        validateForUpdate(credentials);
        // check if the username exists
        final CredentialsEntity entity = credentialsRepository.findOne(id);
        if (entity == null) {
            log.warn("Credentials for '{}' not found", id);
            throw new CredentialsNotFoundException(id);
        }
        if (credentials.getUsername() != null && !credentials.getUsername().isEmpty()) {
            entity.setUsername(credentials.getUsername());
        }
        if (credentials.getPassword() != null && !credentials.getPassword().isEmpty()) {
            hashPassword(entity, credentials.getPassword());
        }
        // FIXME: need to handle error when changePassword() failed
        changePassword(id, credentials.getPassword());
        final CredentialsEntity savedEntity = credentialsRepository.save(entity);
        log.info("Credentials updated: {}", savedEntity);
        return savedEntity;
    }

    private void hashPassword(final CredentialsEntity entity, final String password) {
        entity.setPassword(passwordEncoder.encode(password));
    }

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
