package sg.ncl.service.team.data.jpa.entities;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.team.domain.TeamMember;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "team_members", uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "user_id"}))
public class TeamMemberEntity extends AbstractEntity implements TeamMember {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "team_id", nullable = false, updatable = false)
    private TeamEntity team;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "joined_date", nullable = false)
    private ZonedDateTime joinedDate;

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    public TeamEntity getTeam() {
        return team;
    }

    void setTeam(final TeamEntity team) {
        this.team = team;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public ZonedDateTime getJoinedDate() {
        return joinedDate;
    }

    void setJoinedDate(final ZonedDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }

}
