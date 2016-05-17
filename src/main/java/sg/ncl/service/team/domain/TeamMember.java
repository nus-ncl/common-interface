package sg.ncl.service.team.domain;

import sg.ncl.service.user.domain.User;

import java.time.ZonedDateTime;

/**
 * The {@link TeamMember} interface represents a {@link User} that is part of a {@link Team}.
 *
 * @author Christopher Zhong
 */
public interface TeamMember {

    /**
     * Returns the associated {@link Team}.
     *
     * @return the associated {@link Team}.
     */
    Team getTeam();

    /**
     * Returns the associated {@link User}.
     *
     * @return the associated {@link User}.
     */
    User getUser();

    /**
     * Returns the {@link ZonedDateTime} that the associated {@link User} joined the {@link Team}.
     *
     * @return the {@link ZonedDateTime} that the associated {@link User} joined the {@link Team}.
     */
    ZonedDateTime getJoinedDate();

}
