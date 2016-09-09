package sg.ncl.service.authentication.domain;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsService {

    List<Credentials> getAll();

    Credentials addCredentials(Credentials credentials);

    Credentials updateCredentials(String id, Credentials credentials);

    Credentials updateUsername(String id, Credentials credentials);

    Credentials updatePassword(String id, Credentials credentials);

    Credentials updateStatus(String id, Credentials credentials);

    Credentials addRoles(String id, Credentials credentials);

    Credentials removeRoles(String id, Credentials credentials);

}
