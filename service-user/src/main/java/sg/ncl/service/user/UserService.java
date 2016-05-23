package sg.ncl.service.user;

import org.springframework.stereotype.Service;
import sg.ncl.service.user.data.jpa.entities.UserEntity;
import sg.ncl.service.user.data.jpa.repositories.UserRepository;
import sg.ncl.service.user.domain.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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

    public List<User> get() {
        final List<User> result = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            result.add(user);
        }
        return result;
    }

    public User find(final String id) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            // TODO throw exception
        }
        return one;
    }

    public void update(final String id, final User user) {
        final UserEntity one = userRepository.findOne(id);
        if (one == null) {
            // TODO throw exception
        }
        final UserEntity userEntity = new UserEntity();
        // TODO set only fields that have changed
        userRepository.save(userEntity);
    }

}
