package sg.ncl.service.team.domain;

import java.time.ZonedDateTime;

/**
 * The {@link TeamMember} interface represents a user that is part of a {@link Team}.
 *
 * @author Christopher Zhong
 */
public interface TeamMember {

    /**
     * Returns the associated user ID.
     *
     * @return the associated user ID.
     */
    String getUserId();

    /**
     * Returns the {@link ZonedDateTime} that the associated user joined the {@link Team}.
     *
     * @return the {@link ZonedDateTime} that the associated user joined the {@link Team}.
     */
    ZonedDateTime getJoinedDate();

}
