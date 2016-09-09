package sg.ncl.service.authentication.domain;

import java.util.Set;

/**
 * Interface that represents the credentials of a user.
 *
 * @author Christopher Zhong
 */
public interface Credentials {

    /**
     * Returns the user id associated with this credentials.
     *
     * @return the user id associated with this credentials.
     */
    String getId();

    /**
     * Returns the username.
     *
     * @return the username.
     */
    String getUsername();

    /**
     * Returns the password.
     *
     * @return the password.
     */
    String getPassword();

    /**
     * Returns the status of this credentials.
     *
     * @return the status of this credentials.
     */
    CredentialsStatus getStatus();

    /**
     * Returns the set {@link Role}s associated to this credentials.
     *
     * @return the set {@link Role}s associated to this credentials.
     */
    Set<Role> getRoles();

}
