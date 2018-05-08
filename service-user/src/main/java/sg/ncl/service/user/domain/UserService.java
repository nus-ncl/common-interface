package sg.ncl.service.user.domain;


import java.util.List;
import java.util.Map;

/**
 * @author Christopher Zhong
 */
public interface UserService {

    User createUser(User user);

    List<User> getAll();

    User getUser(final String id);

    User updateUser(final String id, final User user);

    void addTeam(final String userId, final String teamId);

    void removeTeam(final String userId, final String teamId);

    User verifyUserEmail(final String uid, final String email, final String key);

    User updateUserStatus(String id, UserStatus status);

    User removeUser(String id);

    Map<String, String> getPublicKeys(final String userId);

    String addPublicKey(final String publicKey, final String password, final String userId);

}
