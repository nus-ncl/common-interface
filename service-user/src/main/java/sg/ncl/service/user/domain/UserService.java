package sg.ncl.service.user.domain;


import java.util.List;

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

    String getPublicKeys(final String userId);

    String addPublicKey(final String publicKey, final String password, final String userId);

    String deletePublicKey(final String keyId, final String userId);

    User changePasswordStudent(final String uid, final String firstName, final String lastName, final String phone, final String password, final String key);

}
