package sg.ncl.service.user.domain;

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

    /**
     * Returns the {@link Address} of the associated {@link User}.
     *
     * @return the {@link Address} of the associated {@link User}.
     */
    Address getAddress();

    /**
     * Returns the email address of the associated {@link User}.
     *
     * @return the email address of the associated {@link User}.
     */
    String getEmail();

    /**
     * Returns the phone number of the associated {@link User}.
     *
     * @return the phone number of the associated {@link User}.
     */
    String getPhone();

}
