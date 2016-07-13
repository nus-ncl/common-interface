package sg.ncl.service.team.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamMemberStatus;
import sg.ncl.service.team.domain.TeamMemberType;

import java.time.ZonedDateTime;

/**
 * Created by Desmond
 */
public class TeamMemberInfo implements TeamMember {
    private final Long id;
    private final String userId;
    private final ZonedDateTime joinedDate;
    private final TeamMemberType memberType;
    private final TeamMemberStatus memberStatus;

    @JsonCreator
    public TeamMemberInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("userId") final String userId,
            @JsonProperty("joinedDate") final ZonedDateTime joinedDate,
            @JsonProperty("memberType") final TeamMemberType memberType,
            @JsonProperty("memberStatus") final TeamMemberStatus memberStatus) {
        this.id = id;
        this.userId = userId;
        this.joinedDate = joinedDate;
        this.memberType = memberType;
        this.memberStatus = memberStatus;
    }

    public TeamMemberInfo(final TeamMember teamMember) {
        this(teamMember.getId(),
                teamMember.getUserId(),
                teamMember.getJoinedDate(),
                teamMember.getMemberType(),
                teamMember.getMemberStatus());
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
    public TeamMemberType getMemberType() {
        return memberType;
    }

    @Override
    public TeamMemberStatus getMemberStatus() {
        return memberStatus;
    }
}
