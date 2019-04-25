package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.service.analytics.domain.NodesReservation;
import sg.ncl.common.jpa.AbstractEntity;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "nodes_usage_reservation")
public class NodesReservationEntity extends AbstractEntity implements NodesReservation{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "project_id", nullable = false, unique = true)
    private Long projectId;

    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "no_nodes", nullable = false)
    private Integer noNodes;


    @Override
    public String toString() {
        return "NodesReservationEntity{" +
                "id='" + id + '\'' +
                ", project_id='" + projectId + '\'' +
                ", start_date='" + startDate + '\'' +
                ", end_date='" + endDate + '\'' +
                ", no_nodes='" + noNodes + '\'' +
                "} " + super.toString();
    }

}

