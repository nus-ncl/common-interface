package sg.ncl.service.authentication.domain;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsService {

    List<Credentials> getAll();

    Credentials addCredentials(Credentials credentials);

    @Deprecated
    Credentials updateCredentials(String id, Credentials credentials);

    Credentials updateUsername(Credentials credentials);

    Credentials updatePassword(Credentials credentials);

    Credentials updateStatus(Credentials credentials);

    Credentials addRoles(Credentials credentials);

    Credentials removeRoles(Credentials credentials);

}
