package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.DatasetResource;
import sg.ncl.service.data.domain.DatasetResourceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by dcszwang on 10/5/2016.
 */
@Setter
@Getter
@Entity
@Table(name = "dataset_resources")
public class DatasetResourceEntity extends AbstractEntity implements DatasetResource {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatasetResourceType type = DatasetResourceType.INTERNAL;

    @Column(name = "link", nullable = false)
    private String link;

    @Override
    public String toString() {
        return "DatasetResourceEntity{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", link=" + link +
                "} " + super.toString();
    }
}
