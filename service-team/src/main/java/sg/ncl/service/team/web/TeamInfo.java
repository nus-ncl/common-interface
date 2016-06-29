package sg.ncl.service.team.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.domain.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public TeamInfo(@JsonProperty("id") final String id, @JsonProperty("name") final String name, @JsonProperty("description") final String description, @JsonProperty("website") final String website, @JsonProperty("organisationType") final String organisationType, @JsonProperty("visibility") final TeamVisibility visibility, @JsonProperty("privacy") final TeamPrivacy privacy, @JsonProperty("status") final TeamStatus status, @JsonProperty("applicationDate") final ZonedDateTime applicationDate, @JsonProperty("processedDate") final ZonedDateTime processedDate, @JsonProperty("members") final List<TeamMemberEntity> members) {
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

        this.members = new ArrayList<>();

        for (TeamMemberEntity teamMemberEntity : members) {
            this.members.add(new TeamMemberInfo(teamMemberEntity));
        }
    }

    public TeamInfo(final TeamEntity teamEntity) {
        this(teamEntity.getId(),
                teamEntity.getName(),
                teamEntity.getDescription(),
                teamEntity.getWebsite(),
                teamEntity.getOrganisationType(),
                teamEntity.getVisibility(),
                teamEntity.getPrivacy(),
                teamEntity.getStatus(),
                teamEntity.getApplicationDate(),
                teamEntity.getProcessedDate(),
                teamEntity.getMembers());
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

    public List<TeamMemberInfo> getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        TeamInfo that = (TeamInfo) o;

        if (!getId().equals(that.getId())) { return false; }
        if (!getName().equals(that.getName())) { return false; }
        if (!getDescription().equals(that.getDescription())) { return false; }
        return getApplicationDate().equals(that.getApplicationDate());
    }

    @Override
    public int hashCode() {
        int result = 37;
        result = 31 * result + getId().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TeamEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", website='").append(website).append('\'');
        sb.append(", organisationType='").append(organisationType).append('\'');
        sb.append(", visibility=").append(visibility);
        sb.append(", status=").append(status);
        sb.append(", applicationDate=").append(applicationDate);
        sb.append(", processedDate=").append(processedDate);
        sb.append(", members=").append(members);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
