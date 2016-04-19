package sg.ncl.testbed_interface.domain;

/**
 * The {@link PersonalDetails} interface represents the personal details of a {@link User}.
 *
 * @author Christopher Zhong
 */
public interface PersonalDetails {

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
