package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DatasetDownload;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 10/7/2016.
 */
@Entity
@Table(name = "dataset_download_history")
@Getter
@Setter
@Slf4j
public class DatasetDownloadEntity extends AbstractEntity implements DatasetDownload {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "date", nullable = false, updatable = false)
    private ZonedDateTime date;

    @Column(name = "success", nullable = false)
    private boolean success = false;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatasetDownloadHistory{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", date=").append(date);
        sb.append(", success=").append(success);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
