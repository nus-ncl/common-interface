package sg.ncl.domain;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * The {@link Team} interface represents a team.
 *
 * @author Christopher Zhong
 */
public interface Team {

    /**
     * Returns a {@link String} that represents the unique identifier of this {@link Team}.
     *
     * @return a {@link String} that represents the unique identifier of this {@link Team}.
     */
    String getId();

    /**
     * Returns the name of this {@link Team}.
     *
     * @return the name of this {@link Team}.
     */
    String getName();

    /**
     * Returns the description of this {@link Team}.
     *
     * @return the description of this {@link Team}.
     */
    String getDescription();

    /**
     * Returns the {@link TeamVisibility} of this {@link Team}.
     *
     * @return the {@link TeamVisibility} of this {@link Team}.
     */
    TeamVisibility getVisibility();

    /**
     * Returns the {@link TeamPrivacy} of this {@link Team}.
     *
     * @return the {@link TeamPrivacy} of this {@link Team}.
     */
    TeamPrivacy getPrivacy();

    /**
     * Returns the {@link TeamStatus} of this {@link Team}.
     *
     * @return the {@link TeamStatus} of this {@link Team}.
     */
    TeamStatus getStatus();

    /**
     * Returns the time when this {@link Team} was created.
     *
     * @return the time when this {@link Team} was created.
     */
    ZonedDateTime getApplicationDate();

    /**
     * Returns the time when this {@link Team} was approved.
     *
     * @return the time when this {@link Team} was approved.
     */
    ZonedDateTime getProcessedDate();

    /**
     * Returns the members of this {@link Team}.
     *
     * @return the members of this {@link Team}.
     */
    List<? extends TeamMember> getMembers();

}
