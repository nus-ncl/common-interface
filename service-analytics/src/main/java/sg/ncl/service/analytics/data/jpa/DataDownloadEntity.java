package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytics.domain.DataDownload;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */
@Entity
@Table(name = "data_downloads")
@Getter
@Setter
@Slf4j
public class DataDownloadEntity extends AbstractEntity implements DataDownload {

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
        return "DataDownloadEntity{" +
                "id='" + id + '\'' +
                ", data_id=" + dataId +
                ", resource_id=" + resourceId +
                ", download_date=" + downloadDate +
                ", hashed_user_id=" + hashedUserId +
                "} " + super.toString();
    }

}
