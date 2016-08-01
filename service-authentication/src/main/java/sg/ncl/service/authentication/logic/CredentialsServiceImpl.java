package sg.ncl.service.authentication.logic;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

import static sg.ncl.service.authentication.validation.Validator.validateForCreation;
import static sg.ncl.service.authentication.validation.Validator.validateForUpdate;

/**
 * @author Christopher Zhong
 */
@Service
public class CredentialsServiceImpl implements CredentialsService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final AdapterDeterlab adapterDeterlab;

    @Inject
    protected CredentialsServiceImpl(final CredentialsRepository credentialsRepository, final PasswordEncoder passwordEncoder, final AdapterDeterlab adapterDeterlab) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.adapterDeterlab = adapterDeterlab;
    }

    @Transactional
    public List<? extends Credentials> getAll() {
        return credentialsRepository.findAll();
    }

    @Transactional
    public CredentialsEntity addCredentials(@NotNull final Credentials credentials) {
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
                logger.info("Credentials created: {}", savedEntity);
                return savedEntity;
            }
            logger.warn("Username '{}' is already associated with a credentials", credentials.getUsername());
            throw new UsernameAlreadyExistsException(credentials.getUsername());
        }
        logger.warn("User Id '{}' is already associated with a credentials", credentials.getId());
        throw new UserIdAlreadyExistsException(credentials.getId());
    }

    @Transactional
    public void updateCredentials(@NotNull final String id, @NotNull final Credentials credentials) {
        validateForUpdate(credentials);
        // check if the username exists
        final CredentialsEntity entity = credentialsRepository.findOne(id);
        if (entity == null) {
            logger.warn("Credentials for '{}' not found", id);
            throw new CredentialsNotFoundException(id);
        }
        if (credentials.getUsername() != null && !credentials.getUsername().isEmpty()) {
            entity.setUsername(credentials.getUsername());
        }
        if (credentials.getPassword() != null && !credentials.getPassword().isEmpty()) {
            hashPassword(entity, credentials.getPassword());
        }
        // CW: need to handle error when changePassword() failed
        changePassword(id, credentials.getPassword());
        final CredentialsEntity savedEntity = credentialsRepository.save(entity);
        logger.info("Credentials updated: {}", savedEntity);
    }

    private void hashPassword(final CredentialsEntity entity, final String password) {
        entity.setPassword(passwordEncoder.encode(password));
    }


    private void changePassword(String nclUserId, String password) {
        JSONObject adapterObject = new JSONObject();
        // CW: need to handle error when getDeterUserIdByNclUserId() returns nothing
        adapterObject.put("uid", adapterDeterlab.getDeterUserIdByNclUserId(nclUserId));
        adapterObject.put("password1", password);
        adapterObject.put("password2", password);
        logger.info("Credentials to be updated on Deter: {}", adapterObject.toString());
        // CW added: need to handle error if Deter failed to update password
        adapterDeterlab.updateCredentials(adapterObject.toString());
    }

}
