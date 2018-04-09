package sg.ncl.service.team.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Te Ye
 */
@Getter
public class TeamInfo implements Team {

    private final String id;
    private final String name;
    private final String description;
    private final String website;
    private final String organisationType;
    private final TeamVisibility visibility;
    private final TeamPrivacy privacy;
    private final Boolean isClass;
    private final TeamStatus status;
    private final ZonedDateTime applicationDate;
    private final ZonedDateTime processedDate;

    private final List<TeamMemberInfo> members;

    @JsonCreator
    public TeamInfo(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("website") final String website,
            @JsonProperty("organisationType") final String organisationType,
            @JsonProperty("visibility") final TeamVisibility visibility,
            @JsonProperty("isClass") final Boolean isClass,
            @JsonProperty("privacy") final TeamPrivacy privacy,
            @JsonProperty("status") final TeamStatus status,
            @JsonProperty("applicationDate") final ZonedDateTime applicationDate,
            @JsonProperty("processedDate") final ZonedDateTime processedDate,
            @JsonProperty("members") final List<? extends TeamMember> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.organisationType = organisationType;
        this.visibility = visibility;
        this.isClass = isClass;
        this.privacy = privacy;
        this.status = status;
        this.applicationDate = applicationDate;
        this.processedDate = processedDate;
        this.members = members.stream().map(TeamMemberInfo::new).collect(Collectors.toList());
    }

    public TeamInfo(final Team team) {
        this(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getWebsite(),
                team.getOrganisationType(),
                team.getVisibility(),
                team.getIsClass(),
                team.getPrivacy(),
                team.getStatus(),
                team.getApplicationDate(),
                team.getProcessedDate(),
                team.getMembers()
        );
    }
}
