package sg.ncl.testbed_interface.domain;

/**
 * The {@link Credentials} interface represents the credentials of a {@link User}.
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
     * Returns the {@link User} associated to this {@link Credentials}.
     *
     * @return the {@link User} associated to this {@link Credentials}.
     */
    User getUser();
}
