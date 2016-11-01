package sg.ncl.service.realization.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.realization.domain.RealizationLog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 10/27/2016.
 */
@Entity
@Getter
@Setter
@Slf4j
@Table(name = "realizations_log")
public class RealizationLogEntity extends AbstractEntity implements RealizationLog {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "team_id", nullable = false, updatable = false)
    private String teamId;

    @Column(name = "exp_id", nullable = false, updatable = false)
    private Long expId;

    @Column(name = "start_date", nullable = false, updatable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date", updatable = false)
    private ZonedDateTime endDate;

    @Column(name = "num_nodes", nullable = false, updatable = false)
    private Integer numNodes;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RealizationLogEntity{");
        sb.append("id=").append(id);
        sb.append(", team_id=").append(teamId);
        sb.append(", exp_id=").append(expId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", numNodes=").append(numNodes);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}
