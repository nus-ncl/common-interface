package sg.ncl.service.experiment.data.jpa;

import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.experiment.domain.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Desmond
 */
@Entity
@Table(name = "experiments")
public class ExperimentEntity extends AbstractEntity implements Experiment {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "team_id", nullable = false, updatable = false)
    private String teamId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "ns_file", nullable = false)
    private String nsFile;

    @Column(name = "idle_swap", nullable = false)
    private Integer idleSwap;

    @Column(name = "max_duration", nullable = false)
    private Integer maxDuration;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getNsFile() {
        return nsFile;
    }

    public void setNsFile(String nsFile) {
        this.nsFile = nsFile;
    }

    @Override
    public Integer getIdleSwap() {
        return idleSwap;
    }

    public void setIdleSwap(Integer idleSwap) {
        this.idleSwap = idleSwap;
    }

    @Override
    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Experiment that = (Experiment) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExperimentEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", teamId=").append(teamId);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", nsFile=").append(nsFile);
        sb.append(", idleSwap=").append(idleSwap);
        sb.append(", maxDuration=").append(maxDuration);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
