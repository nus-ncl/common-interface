package sg.ncl.service.team.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sg.ncl.service.team.data.jpa.entities.TeamMemberEntity;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamMemberType;

import java.time.ZonedDateTime;

/**
 * Created by Desmond
 */
public class TeamMemberInfo implements TeamMember {
    private final Long id;
    private final String userId;
    private final ZonedDateTime joinedDate;
    private final TeamMemberType teamMemberType;

    @JsonCreator
    public TeamMemberInfo(@JsonProperty("id") final Long id, @JsonProperty("userId") final String userId, @JsonProperty("joinedDate") final ZonedDateTime joinedDate, @JsonProperty("teamMemberType") final TeamMemberType teamMemberType) {
        this.id = id;
        this.userId = userId;
        this.joinedDate = joinedDate;
        this.teamMemberType = teamMemberType;
    }

    public TeamMemberInfo(final TeamMemberEntity teamMemberEntity) {
        this(teamMemberEntity.getId(),
                teamMemberEntity.getUserId(),
                teamMemberEntity.getJoinedDate(),
                teamMemberEntity.getTeamMemberType());
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public ZonedDateTime getJoinedDate() {
        return joinedDate;
    }

    @Override
    public TeamMemberType getTeamMemberType() {
        return teamMemberType;
    }
}