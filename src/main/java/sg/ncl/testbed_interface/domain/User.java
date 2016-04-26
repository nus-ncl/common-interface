package sg.ncl.testbed_interface.domain;

import java.time.ZonedDateTime;

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
     * Returns the {@link UserDetails} of this {@link User}.
     *
     * @return the {@link UserDetails} of this {@link User}.
     */
    UserDetails getUserDetails();

    /**
     * Returns {@code true} if the email is verified, {@code false} otherwise.
     *
     * @return {@code true} if the email is verified, {@code false} otherwise.
     */
    boolean isEmailVerified();

    /**
     * Returns the {@link UserStatus} of this {@link User}.
     *
     * @return the {@link UserStatus} of this {@link User}.
     */
    UserStatus getStatus();

    /**
     * Returns the {@link ZonedDateTime} of when this {@link User} applied for an account.
     *
     * @return the {@link ZonedDateTime} of when this {@link User} applied for an account.
     */
    ZonedDateTime getRegistrationDate();

    /**
     * Returns the {@link ZonedDateTime} of when this {@link User}'s application was processed.
     *
     * @return the {@link ZonedDateTime} of when this {@link User}'s application was processed.
     */
    ZonedDateTime getProcessedDate();

}
