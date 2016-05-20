package sg.ncl.service.user;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Inject
    protected UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
