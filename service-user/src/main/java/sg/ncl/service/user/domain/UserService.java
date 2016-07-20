package sg.ncl.service.user.domain;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface UserService {

    User createUser(User user);

    List<User> getAll();

    User findUser(final String id);

    void updateUser(final String id, final User user);

    void addTeam(final String userId, final String teamId);

}