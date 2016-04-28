package sg.ncl.testbed_interface.repositories.jpa.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class AbstractEntity {

//    @Column(name = "created_by")
//    @CreatedBy
//    private String createdBy;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private ZonedDateTime createdDate;

//    @Column(name = "last_modified_by")
//    @LastModifiedBy
//    private String lastModifiedBy;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    protected void setCreatedDate(final ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    protected void setLastModifiedDate(final ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getVersion() {
        return version;
    }

    protected void setVersion(final Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractEntity{");
        sb.append("createdDate=").append(createdDate);
        sb.append(", lastModifiedDate=").append(lastModifiedDate);
        sb.append(", version=").append(version);
        sb.append('}');
        return sb.toString();
    }

}
