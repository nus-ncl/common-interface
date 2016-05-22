package sg.ncl.service.authentication;

import org.springframework.stereotype.Service;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;

import javax.inject.Inject;

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

}
