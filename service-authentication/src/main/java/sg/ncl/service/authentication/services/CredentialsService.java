package sg.ncl.service.authentication.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.common.exceptions.BadRequestException;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.NullPasswordException;
import sg.ncl.service.authentication.exceptions.NullUserIdException;
import sg.ncl.service.authentication.exceptions.NullUsernameException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Service
public class CredentialsService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    protected CredentialsService(final CredentialsRepository credentialsRepository, final PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<? extends Credentials> getAll() {
        return credentialsRepository.findAll();
    }

    @Transactional
    public CredentialsEntity addCredentials(final Credentials credentials) {
        if (credentials.getUsername() == null) {
            logger.warn("Username is null");
            throw new NullUsernameException();
        }
        if (credentials.getPassword() == null) {
            logger.warn("Password is null");
            throw new NullPasswordException();
        }
        if (credentials.getUserId() == null) {
            logger.warn("User ID is null");
            throw new NullUserIdException();
        }
        // check if the username already exists
        if (credentialsRepository.findByUsername(credentials.getUsername()) == null) {
            // check if the user id already exists
            if (credentialsRepository.findByUserId(credentials.getUserId()) == null) {
                final CredentialsEntity entity = new CredentialsEntity();
                entity.setUsername(credentials.getUsername());
                setPassword(entity, credentials.getPassword());
                entity.setUserId(credentials.getUserId());
                entity.setStatus(CredentialsStatus.ACTIVE);
                final CredentialsEntity savedEntity = credentialsRepository.save(entity);
                logger.info("New credentials `{}`", savedEntity);
                return savedEntity;
            }
            logger.warn("User ID `{}` is already associated with a credentials", credentials.getUserId());
            throw new UserIdAlreadyExistsException(credentials.getUserId());
        }
        logger.warn("Username `{}` is already associated with a credentials", credentials.getUsername());
        throw new UsernameAlreadyExistsException(credentials.getUsername());
    }

    @Transactional
    public void updatePassword(final Credentials credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new BadRequestException();
        }
        // check if the username exists
        final CredentialsEntity entity = credentialsRepository.findByUsername(credentials.getUsername());
        if (entity == null) {
            throw new CredentialsNotFoundException();
        }
        setPassword(entity, credentials.getPassword());
    }

    private void setPassword(final CredentialsEntity entity, final String password) {
        entity.setPassword(passwordEncoder.encode(password));
    }

}
