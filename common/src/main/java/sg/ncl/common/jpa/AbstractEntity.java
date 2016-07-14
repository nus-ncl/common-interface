package sg.ncl.common.jpa;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

//    @Column(name = "created_by")
//    @CreatedBy
//    private String createdBy;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private ZonedDateTime createdDate;

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

//    @Column(name = "last_modified_by")
//    @LastModifiedBy
//    private String lastModifiedBy;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", version=" + version +
                '}';
    }

}
