package sg.ncl.service.analytic.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytic.domain.DatasetDownloads;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.time.ZonedDateTime;



/**
 * @author: Tran Ly Vu
 */


@Entity
@Table(name = "dataset_downloads")
@Getter
@Setter
@Slf4j
public class DatasetDownloadsEntity extends AbstractEntity implements DatasetDownloads {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "data_id", nullable = false)
    private Long dataId;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "download_date", nullable = false)
    private ZonedDateTime downloadDate;

    @Column(name = "hashed_user_id", nullable = false)
    private String hashedUserId;

    @Override
    public String toString() {
        return "DataEntity{" +
                "id='" + id + '\'' +
                ", data_id=" + dataId +
                ", resource_id=" + resourceId +
                ", date=" + downloadDate +
                ", hashed_user_id=" + hashedUserId +
                "} " + super.toString();
    }

}
