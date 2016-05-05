package sg.ncl.domain;

/**
 * The {@link UserCredentials} interface represents the credentials of a {@link User}.
 *
 * @author Christopher Zhong
 */
public interface UserCredentials {

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
     * Returns the {@link User} associated to this {@link UserCredentials}.
     *
     * @return the {@link User} associated to this {@link UserCredentials}.
     */
    User getUser();

    /**
     * Returns the {@link UserCredentialsStatus} of this {@link UserCredentials}.
     *
     * @return the {@link UserCredentialsStatus} of this {@link UserCredentials}.
     */
    UserCredentialsStatus getStatus();

}
