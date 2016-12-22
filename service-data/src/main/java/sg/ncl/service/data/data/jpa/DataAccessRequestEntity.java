package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DataAccessRequest;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by dcsjnh on 12/22/2016.
 */
@Setter
@Getter
@Entity
@Table(name = "data_access_requests")
public class DataAccessRequestEntity extends AbstractEntity implements DataAccessRequest {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "data_id")
    private Long dataId;

    @Column(name = "requester_id", nullable = false, updatable = false)
    private String requesterId;

    @Type(type = "text")
    @Column(name = "reason")
    private String reason;

    @Column(name = "request_date")
    private ZonedDateTime requestDate;

    @Column(name = "approved_date")
    private ZonedDateTime approvedDate;

    @Override
    public String toString() {
        return "DataAccessRequestEntity{" +
                "id='" + id + "', " +
                "dataId='" + dataId + "', " +
                "requesterId='" + requesterId + "', " +
                "reason='" + reason + "', " +
                "requestDate='" + requestDate + "', " +
                "approvedDate='" + approvedDate + "', " +
                "} " + super.toString();
    }

}
