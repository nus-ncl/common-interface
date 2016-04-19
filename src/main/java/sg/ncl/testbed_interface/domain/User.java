package sg.ncl.testbed_interface.domain;

/**
 * The {@link User} interface represents a user.
 *
 * @author Christopher Zhong
 */
public interface User {

    /**
     * Returns a {@link String} that represents the unique identifier of this {@link User}.
     *
     * @return a {@link String} that represents the unique identifier of this {@link User}.
     */
    String getId();

    /**
     * Returns the {@link PersonalDetails} of this {@link User}.
     *
     * @return the {@link PersonalDetails} of this {@link User}.
     */
    PersonalDetails getPersonalDetails();

}
