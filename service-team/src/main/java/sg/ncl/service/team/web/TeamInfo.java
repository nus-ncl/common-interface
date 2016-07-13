package sg.ncl.service.team.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sg.ncl.service.team.domain.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Te Ye
 */
public class TeamInfo implements Team {

    private final String id;
    private final String name;
    private final String description;
    private final String website;
    private final String organisationType;
    private final TeamVisibility visibility;
    private final TeamPrivacy privacy;
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
        this.privacy = privacy;
        this.status = status;
        this.applicationDate = applicationDate;
        this.processedDate = processedDate;
        this.members = members.stream().map(TeamMemberInfo::new).collect(Collectors.toList());
    }

    public TeamInfo(final Team team) {
        this(team.getId(),
                team.getName(),
                team.getDescription(),
                team.getWebsite(),
                team.getOrganisationType(),
                team.getVisibility(),
                team.getPrivacy(),
                team.getStatus(),
                team.getApplicationDate(),
                team.getProcessedDate(),
                team.getMembers());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public String getOrganisationType() {
        return organisationType;
    }

    public TeamVisibility getVisibility() {
        return visibility;
    }

    public TeamPrivacy getPrivacy() {
        return privacy;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public ZonedDateTime getApplicationDate() {
        return applicationDate;
    }

    public ZonedDateTime getProcessedDate() {
        return processedDate;
    }

    public List<? extends TeamMember> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "TeamInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", organisationType='" + organisationType + '\'' +
                ", visibility=" + visibility +
                ", privacy=" + privacy +
                ", status=" + status +
                ", applicationDate=" + applicationDate +
                ", processedDate=" + processedDate +
                ", members=" + members +
                '}';
    }
}
