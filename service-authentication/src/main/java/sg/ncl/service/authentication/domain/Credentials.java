package sg.ncl.service.authentication.domain;

/**
 * Interface that represents the credentials of a user.
 *
 * @author Christopher Zhong
 */
public interface Credentials {

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
     * Returns the user id associated with this credentials.
     *
     * @return the user id associated with this credentials.
     */
    String getUserId();

    /**
     * Returns the status of this credentials.
     *
     * @return the status of this credentials.
     */
    CredentialsStatus getStatus();

}
