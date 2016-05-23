package sg.ncl.service.authentication;

import org.springframework.stereotype.Service;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;

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
        return UUID.randomUUID().toString();
    }

    public void logout() {}

}
