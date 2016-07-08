package sg.ncl.service.team.data.jpa;

import org.hibernate.annotations.GenericGenerator;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.team.domain.*;

import javax.persistence.*;
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

    public void addMember(final TeamMember member) {
        final String userId = member.getUserId();
        if (members.containsKey(userId)) {
//            throw new UserAlreadyInTeam();
        } else {
            TeamMemberEntity entity = new TeamMemberEntity();
            entity.setUserId(userId);
            entity.setTeam(this);
            entity.setJoinedDate(ZonedDateTime.now());
            entity.setMemberType(member.getMemberType());
            members.put(userId, entity);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Team that = (Team) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
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
                "} " + super.toString();
    }
}
