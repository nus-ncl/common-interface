package sg.ncl.testbed_interface.domain;

/**
 * The {@link UserDetails} interface represents the personal details of a {@link User}.
 *
 * @author Christopher Zhong
 */
public interface UserDetails {

    /**
     * Returns the first name of the associated {@link User}.
     *
     * @return the first name of the associated {@link User}.
     */
    String getFirstName();

    /**
     * Returns the last name of the associated {@link User}.
     *
     * @return the last name of the associated {@link User}.
     */
    String getLastName();

}
