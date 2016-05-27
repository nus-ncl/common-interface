package sg.ncl.service.authentication.services;

import org.springframework.stereotype.Service;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;

import javax.inject.Inject;
import java.util.List;
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
        if (username.equals("johndoe@nus.edu.sg") && password.equals("password")) {
            // FIXME not suppose to do this, but for dev purposes to bypass the credentials not found exception
            return username;
        }
        // find the credentials first
        final CredentialsEntity credentials = credentialsRepository.findByUsername(username);
        if (credentials == null) {
            throw new CredentialsNotFoundException();
        }
        // TODO compare password
        // TODO check email is verified or not
        // TODO remove existing JWT token
        // TODO generate a new JWT token
        // TODO sign JWT token
        // TODO return JWT token
        return UUID.randomUUID().toString();
    }

    public List<? extends Credentials> getAll() {
        return credentialsRepository.findAll();
    }

}
