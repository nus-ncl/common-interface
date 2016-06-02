package sg.ncl.service.team.dtos;

import sg.ncl.service.team.data.jpa.entities.TeamMemberEntity;
import sg.ncl.service.team.domain.TeamMember;

import java.time.ZonedDateTime;

/**
 * Created by Desmond
 */
public class TeamMemberInfo implements TeamMember {
    private final Long id;
    private final TeamInfo team;
    private final String userId;
    private final ZonedDateTime joinedDate;

    public TeamMemberInfo(final Long id, final TeamInfo team, final String userId, final ZonedDateTime joinedDate) {
        this.id = id;
        this.team = team;
        this.userId = userId;
        this.joinedDate = joinedDate;
    }

    public TeamMemberInfo(final TeamMemberEntity teamMemberEntity) {
        this(teamMemberEntity.getId(),
                new TeamInfo(teamMemberEntity.getTeam()),
                teamMemberEntity.getUserId(),
                teamMemberEntity.getJoinedDate());
    }

    public Long getId() {
        return id;
    }

    public TeamInfo getTeam() {
        return team;
    }

    public String getUserId() {
        return userId;
    }

    public ZonedDateTime getJoinedDate() {
        return joinedDate;
    }
}
