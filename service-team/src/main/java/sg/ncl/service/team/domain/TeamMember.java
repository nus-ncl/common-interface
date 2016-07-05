package sg.ncl.service.team.domain;


import java.time.ZonedDateTime;

/**
 * The {@link TeamMember} interface represents a user that is part of a {@link Team}.
 *
 * @author Christopher Zhong
 */
public interface TeamMember {

    /**
     * Returns a {@link String} that represents the unique identifier of this {@link Team}.
     *
     * @return a {@link String} that represents the unique identifier of this {@link Team}.
     */
    Long getId();

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

    /**
     * Returns the associated team membership.
     *
     * @return the associated team membership.
     */
    TeamMemberType getTeamMemberType();

}
