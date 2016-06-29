package sg.ncl.service.team.data.jpa;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.web.TeamMemberInfo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "teams")
public class TeamEntity extends AbstractEntity implements Team {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "website", nullable = false)
    private String website;

    @Column(name = "organisation_type", nullable = false)
    private String organisationType;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamVisibility visibility = TeamVisibility.PUBLIC;

    @Column(name = "privacy", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamPrivacy privacy = TeamPrivacy.OPEN;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamStatus status = TeamStatus.PENDING;

    @Column(name = "application_date", nullable = false)
    private ZonedDateTime applicationDate;

    @Column(name = "processed_date")
    private ZonedDateTime processedDate;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "team")
    @MapKey(name = "userId")
    private final Map<String, TeamMemberEntity> members = new HashMap<>();

    @Override
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(String organisationType) {
        this.organisationType = organisationType;
    }

    @Override
    public TeamVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(final TeamVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public TeamPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(final TeamPrivacy privacy) {
        this.privacy = privacy;
    }

    @Override
    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(final TeamStatus status) {
        this.status = status;
    }

    @Override
    public ZonedDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(ZonedDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public ZonedDateTime getProcessedDate() {
        return processedDate;
    }

    void setProcessedDate(ZonedDateTime processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public List<TeamMemberEntity> getMembers() {
        return new ArrayList<>(members.values());
    }

    public void addMember(final TeamMemberInfo teamMemberInfo) {
        String userId = teamMemberInfo.getUserId();
        if (members.containsKey(userId)) {
//            throw new UserAlreadyInTeam();
        } else {
            TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
            teamMemberEntity.setUserId(userId);
            teamMemberEntity.setTeam(this);
            teamMemberEntity.setJoinedDate(ZonedDateTime.now());
            teamMemberEntity.setTeamMemberType(teamMemberInfo.getTeamMemberType());
            members.put(userId, teamMemberEntity);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Team that = (Team) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TeamEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
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
