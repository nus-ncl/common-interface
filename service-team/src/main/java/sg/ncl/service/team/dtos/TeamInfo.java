package sg.ncl.service.team.dtos;

import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.entities.TeamMemberEntity;
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
    private final TeamVisibility visibility;
    private final TeamPrivacy privacy;
    private final TeamStatus status;
    private final ZonedDateTime applicationDate;
    private final ZonedDateTime processedDate;
    private final List<TeamMemberInfo> members;

    public TeamInfo(final String id, final String name, final String description, final TeamVisibility visibility, final TeamPrivacy privacy, final TeamStatus status, final ZonedDateTime applicationDate, final ZonedDateTime processedDate, final List<TeamMemberEntity> members) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
