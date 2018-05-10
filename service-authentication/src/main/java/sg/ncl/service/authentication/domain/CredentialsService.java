package sg.ncl.service.authentication.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * @author Christopher Zhong
 */
public interface CredentialsService {

    List<Credentials> getAll();

    Credentials addCredentials(Credentials credentials);

    Credentials updateCredentials(String id, Credentials credentials, Claims claims);

    Credentials updateUsername(String id, Credentials credentials);

    Credentials updatePassword(String id, Credentials credentials);

    Credentials updateStatus(String id, Credentials credentials);

    Credentials addRoles(String id, Credentials credentials);

    Credentials removeRoles(String id, Credentials credentials);

    void removeCredentials(String id);

    void addPasswordResetRequest(String jsonString);

    Credentials resetPassword(String jsonString);

    void SendEmailToClassMember(String username);
}
