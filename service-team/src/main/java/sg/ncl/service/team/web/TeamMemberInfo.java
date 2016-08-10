package sg.ncl.service.team.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.TeamMember;

import java.time.ZonedDateTime;

/**
 * Created by Desmond
 */
@Getter
public class TeamMemberInfo implements TeamMember {
    private final Long id;
    private final String userId;
    private final ZonedDateTime joinedDate;
    private final MemberType memberType;
    private final MemberStatus memberStatus;

    @JsonCreator
    public TeamMemberInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("userId") final String userId,
            @JsonProperty("joinedDate") final ZonedDateTime joinedDate,
            @JsonProperty("memberType") final MemberType memberType,
            @JsonProperty("memberStatus") final MemberStatus memberStatus) {
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

}
