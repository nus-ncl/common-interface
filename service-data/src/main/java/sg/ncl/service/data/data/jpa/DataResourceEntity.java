package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DataResource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Override
    public String toString() {
        return "DataResourceEntity{" +
                "id='" + id + '\'' +
                ", uri=" + uri +
                "} " + super.toString();
    }
}
