package sg.ncl.service.team.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;

import java.time.ZonedDateTime;

/**
 * The {@link TeamMember} interface represents a user that is part of a {@link Team}.
 *
 * @author Christopher Zhong
 */
@JsonDeserialize(as = TeamMemberEntity.class)
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

    /**
     * Returns the associated team membership.
     *
     * @return the associated team membership.
     */
    TeamMemberType getTeamMemberType();

}
