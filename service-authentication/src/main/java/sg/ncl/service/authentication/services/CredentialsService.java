package sg.ncl.service.authentication.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.common.exceptions.BadRequestException;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.UserIdAlreadyExistsException;
import sg.ncl.service.authentication.exceptions.UsernameAlreadyExistsException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Service
public class CredentialsService {

    private final CredentialsRepository credentialsRepository;

    @Inject
    protected CredentialsService(final CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional(readOnly = true)
    public List<? extends Credentials> getAll() {
        return credentialsRepository.findAll();
    }

    @Transactional
    public void addCredentials(final Credentials credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null || credentials.getUserId() == null) {
            throw new BadRequestException();
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
                credentialsRepository.save(entity);
            }
            throw new UserIdAlreadyExistsException();
        }
        throw new UsernameAlreadyExistsException();
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

    private void setPassword(CredentialsEntity entity, String password) {
        // FIXME use spring security bccrypt
        entity.setPassword(password);
    }

}
