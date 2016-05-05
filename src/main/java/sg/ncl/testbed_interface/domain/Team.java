package sg.ncl.testbed_interface.domain;

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

    String getName();

    String getDescription();

    TeamVisibility getVisibility();

    TeamStatus getStatus();

    ZonedDateTime getApplicationDate();

    ZonedDateTime getProcessedDate();

    List<? extends TeamMember> getMembers();

}
