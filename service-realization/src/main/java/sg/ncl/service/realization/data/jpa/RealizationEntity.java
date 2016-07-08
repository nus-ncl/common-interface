package sg.ncl.service.realization.data.jpa;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.realization.domain.Realization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Christopher Zhong
 */
@Entity
@Table(name = "realizations")
public class RealizationEntity extends AbstractEntity implements Realization {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "experiment_id", nullable = false, updatable = false)
    private Long experimentId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "team_id", nullable = false, updatable = false)
    private String teamId;

    @Column(name = "num_nodes", nullable = false, updatable = false)
    private Integer numberOfNodes;

    @Column(name = "idle_minutes", nullable = false, updatable = false)
    private Long idleMinutes;

    @Column(name = "running_minutes", nullable = false, updatable = false)
    private Long runningMinutes;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public Integer getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(Integer numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    @Override
    public Long getIdleMinutes() {
        return idleMinutes;
    }

    public void setIdleMinutes(Long idleMinutes) {
        this.idleMinutes = idleMinutes;
    }

    @Override
    public Long getRunningMinutes() {
        return runningMinutes;
    }

    public void setRunningMinutes(Long runningMinutes) {
        this.runningMinutes = runningMinutes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Realization that = (Realization) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RealizationEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
