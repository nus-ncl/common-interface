package sg.ncl.testbed_interface.i;

/**
 * The {@link Credentials} interface represents the credentials of a {@link User}.
 * @author Christopher Zhong
 */
public interface Credentials {

    /**
     * Returns the username.
     * @return the username.
     */
    String getUsername();

    /**
     * Returns the password.
     * @return the password.
     */
    String getPassword();

    /**
     * Returns the hash type.
     * @return the hash type.
     */
    String getHashType();

}
