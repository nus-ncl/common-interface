package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytics.domain.DataPublicDownload;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "data_public_downloads")
@Getter
@Setter
@Slf4j
public class DataPublicDownloadEntity extends AbstractEntity implements DataPublicDownload {

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

    @Column(name = "public_user_id", nullable = false)
    private Long publicUserId;

    @Override
    public String toString() {
        return "DataPublicDownloadEntity{" +
                "id='" + id + '\'' +
                ", data_id=" + dataId +
                ", resource_id=" + resourceId +
                ", download_date=" + downloadDate +
                ", public_user_id=" + publicUserId +
                "} " + super.toString();
    }

}
