package sg.ncl.service.team.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamPrivacy;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;

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
@Getter
@Setter
@Entity
@Slf4j
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
    public List<TeamMemberEntity> getMembers() {
        return new ArrayList<>(members.values());
    }

    public TeamMember addMember(final TeamMember member) {
        final String userId = member.getUserId();
        // user removed from a team has status REJECTED
        if (members.containsKey(userId)) {
            TeamMemberEntity teamMemberEntity = members.get(userId);
            if (teamMemberEntity.getMemberStatus() == MemberStatus.REJECTED) {
                teamMemberEntity.setMemberStatus(MemberStatus.PENDING);
                teamMemberEntity.setJoinedDate(ZonedDateTime.now());
                members.put(userId, teamMemberEntity);
                return members.get(userId);
            } else {
                return null;
            }
        } else {
            TeamMemberEntity entity = new TeamMemberEntity();
            entity.setUserId(userId);
            entity.setTeam(this);
            entity.setJoinedDate(ZonedDateTime.now());
            entity.setMemberType(member.getMemberType());
            entity.setMemberStatus(member.getMemberStatus());
            members.put(userId, entity);
            return members.get(userId);
        }
    }

    public TeamMember getMember(final String userId) {
        if (members.containsKey(userId)) {
            return members.get(userId);
        }
        return null;
    }

    public TeamMember changeMemberStatus(final TeamMember member, final MemberStatus memberStatus) {
        final String userId = member.getUserId();
        if (members.containsKey(userId)) {
            TeamMemberEntity entity = members.get(userId);
            entity.setMemberStatus(memberStatus);
            members.put(userId, entity);
            return members.get(userId);
        }
        return null;
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
