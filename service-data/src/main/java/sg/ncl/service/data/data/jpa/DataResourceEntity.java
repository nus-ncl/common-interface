package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DataResource;

import javax.persistence.*;

/**
 * Created by dcszwang on 10/5/2016.
 */
@Setter
@Getter
@Entity
@Table(name = "data_resources")
public class DataResourceEntity extends AbstractEntity implements DataResource {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Type(type = "text")
    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "is_malicious", nullable = false)
    @Type(type = "yes_no")
    private boolean malicious = false;

    @Column(name = "is_scanned", nullable = false)
    @Type(type = "yes_no")
    private boolean scanned = false;

    @ManyToOne
    @JoinColumn(name = "data_id")
    private DataEntity dataEntity;

    @Override
    public String toString() {
        return "DataResourceEntity{" +
                "id='" + id + '\'' +
                ", uri=" + uri +
                ", malicious=" + malicious +
                ", scanned=" + scanned +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DataResourceEntity that = (DataResourceEntity) o;

        if (!uri.equals(that.uri)) {
            return false;
        }

        if (malicious != that.malicious) {
            return false;
        }

        if (scanned != that.scanned) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
