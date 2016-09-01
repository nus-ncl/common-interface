package sg.ncl.service.user.domain;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * The {@link User} interface represents a user.
 *
 * @author Christopher Zhong
 */
public interface User {

    /**
     * The {@link Role} enumerates all the roles that a {@link User} can play.
     *
     * @author Te Ye
     */
    enum Role {
        ADMIN, USER
    }

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
     * Returns a set {@link Role}s that this {@link User} plays.
     *
     * @return a set {@link Role}s that this {@link User} plays.
     */
    Set<Role> getRoles();

    /**
     * Returns the {@link ZonedDateTime} of when this {@link User} applied for an account.
     *
     * @return the {@link ZonedDateTime} of when this {@link User} applied for an account.
     */
    ZonedDateTime getApplicationDate();

    /**
     * Returns the {@link ZonedDateTime} of when this {@link User}'s application was processed.
     *
     * @return the {@link ZonedDateTime} of when this {@link User}'s application was processed.
     */
    ZonedDateTime getProcessedDate();

    /**
     * Returns a list of {@link LoginActivity} for this {@link User}.
     *
     * @return a list of {@link LoginActivity} for this {@link User}.
     */
    List<? extends LoginActivity> getLoginActivities();

    /**
     * Returns a list of teams for this {@link User}.
     *
     * @return a list of teams for this {@link User}.
     */
    List<String> getTeams();

}
