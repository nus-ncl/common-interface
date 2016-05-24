package sg.ncl.service.authentication;

import org.springframework.stereotype.Service;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;

import javax.inject.Inject;
import java.util.UUID;

/**
 * @author Christopher Zhong
 */
@Service
public class AuthenticationService {

    private final CredentialsRepository credentialsRepository;

    @Inject
    protected AuthenticationService(final CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    public String login(final String username, String password) {
        // find the credentials first
        final CredentialsEntity credentials = credentialsRepository.findByUsername(username);
        if (credentials == null) {
            throw new CredentialsNotFoundException();
        }
        // TODO compare password
        // TODO remove existing JWT token
        // TODO generate a new JWT token
        // TODO sign JWT token
        // TODO return JWT token
        return UUID.randomUUID().toString();
    }

    public void logout() {}

}
