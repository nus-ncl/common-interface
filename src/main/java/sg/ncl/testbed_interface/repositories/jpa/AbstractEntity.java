package sg.ncl.testbed_interface.repositories.jpa;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class AbstractEntity {

//    @Column(name = "creadted_by")
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

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

}
