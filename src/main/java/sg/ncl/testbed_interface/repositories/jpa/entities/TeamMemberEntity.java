package sg.ncl.testbed_interface.repositories.jpa.entities;

import sg.ncl.testbed_interface.domain.TeamMember;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "team_members", uniqueConstraints = {@UniqueConstraint(columnNames = {"team_id", "user_id"})})
public class TeamMemberEntity extends AbstractEntity implements TeamMember {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @ManyToOne(cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team = null;

    @ManyToOne(cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user = null;

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    @Override
    public TeamEntity getTeam() {
        return team;
    }

    void setTeam(final TeamEntity team) {
        this.team = team;
    }

    @Override
    public UserEntity getUser() {
        return user;
    }

    void setUser(final UserEntity user) {
        this.user = user;
    }

}
