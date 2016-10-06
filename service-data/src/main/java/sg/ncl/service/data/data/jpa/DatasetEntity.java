package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.Dataset;
import sg.ncl.service.data.domain.DatasetAccessibility;
import sg.ncl.service.data.domain.DatasetStatus;
import sg.ncl.service.data.domain.DatasetVisibility;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcszwang on 10/5/2016.
 */
@Entity
@Table(name = "datasets", indexes = @Index(columnList = "name"))
@Getter
@Setter
@Slf4j
public class DatasetEntity extends AbstractEntity implements Dataset {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "owner_id", nullable = false, updatable = false)
    private String ownerId;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatasetVisibility visibility = DatasetVisibility.PUBLIC;

    @Column(name = "accessibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatasetAccessibility accessibility = DatasetAccessibility.OPEN;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatasetStatus status = DatasetStatus.COMPLETE;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dataset_id")
    private List<DatasetResourceEntity> resources = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "users_datasets", joinColumns = @JoinColumn(name = "dataset_id", nullable = false, updatable = false), indexes = {@Index(columnList = "user_id"), @Index(columnList = "dataset_id")}, uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "dataset_id"}))
    @Column(name = "user_id", nullable = false, updatable = false)
    private final List<String> approvedUsers = new ArrayList<>();

    public void addApprovedUser(final String userId) {
        if(approvedUsers.contains(userId)) {
            log.warn("User {} was already approved to access dataset {}", userId, name);
            return;
        }
        approvedUsers.add(userId);
        log.info("User {} approved to access dataset {}", userId, name);
    }

    public void removeApprovedUser(final String userId) {
        if(!approvedUsers.contains(userId)) {
            log.warn("User {} not in the approved list for dataset {}", userId, name);
            return;
        }
        approvedUsers.remove(userId);
        log.info("User {} removed from the approved list for dataset {}", userId, name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Dataset that = (Dataset) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "DatasetEntity{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", ownerId=" + ownerId +
                ", visibility=" + visibility +
                ", accessibility=" + accessibility +
                ", status=" + status +
                ", resources=" + resources +
                ", approvedUsers=" + approvedUsers +
                "} " + super.toString();
    }
}
