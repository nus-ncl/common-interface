package sg.ncl.service.data.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.data.domain.Data;
import sg.ncl.service.data.domain.DataAccessibility;
import sg.ncl.service.data.domain.DataResource;
import sg.ncl.service.data.domain.DataVisibility;

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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcszwang on 10/5/2016.
 */
@Entity
@Table(name = "data", indexes = @Index(columnList = "name"))
@Getter
@Setter
@Slf4j
public class DataEntity extends AbstractEntity implements Data {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, updatable = false)
    private String name;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    @Column(name = "contributor_id", nullable = false, updatable = false)
    private String contributorId;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataVisibility visibility = DataVisibility.PUBLIC;

    @Column(name = "accessibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataAccessibility accessibility = DataAccessibility.OPEN;

    @Column(name = "released_date", nullable = false)
    private ZonedDateTime releasedDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataEntity", orphanRemoval = true)
    private List<DataResourceEntity> resources = new ArrayList<>();



    @ElementCollection
    @CollectionTable(name = "data_users", joinColumns = @JoinColumn(name = "data_id", nullable = false, updatable = false), indexes = {@Index(columnList = "data_id"), @Index(columnList = "user_id")}, uniqueConstraints = @UniqueConstraint(columnNames = {"data_id", "user_id"}))
    @Column(name = "user_id", nullable = false, updatable = false)
    private final List<String> approvedUsers = new ArrayList<>();

    public void addApprovedUser(final String userId) {
        if(approvedUsers.contains(userId)) {
            log.warn("User {} was already approved to access data {}", userId, name);
            return;
        }
        approvedUsers.add(userId);
        log.info("User {} approved to access data {}", userId, name);
    }

    public void removeApprovedUser(final String userId) {
        if(!approvedUsers.contains(userId)) {
            log.warn("User {} not in the approved list for data {}", userId, name);
            return;
        }
        approvedUsers.remove(userId);
        log.info("User {} removed from the approved list for data {}", userId, name);
    }

    public void addResource(DataResourceEntity dataResourceEntity) {
        if (!resources.contains(dataResourceEntity)) {
            resources.add(dataResourceEntity);
            dataResourceEntity.setDataEntity(this);
            log.info("Resource {} added in the list for data {}", dataResourceEntity, name);
        } else {
            log.info("Resource {} is already in the list for data {}", dataResourceEntity, name);
        }
    }

    public void removeResource(DataResourceEntity dataResourceEntity) {
        if (resources.contains(dataResourceEntity)) {
            resources.remove(dataResourceEntity);
            log.info("Resource {} removed from the list for data {}", dataResourceEntity, name);
        } else {
            log.info("Resource {} not in the list for data {}", dataResourceEntity, name);
        }
    }

    public DataResource editResourceMalicious(DataResource dataResource, boolean isMalicious) {
        for (DataResourceEntity dataResourceEntity : resources) {
            if (dataResourceEntity.getUri().equals(dataResource.getUri()) && dataResourceEntity.isMalicious() != isMalicious) {
                dataResourceEntity.setMalicious(isMalicious);
                log.info("Data resource {}: is {}", dataResource.getUri(), isMalicious);
                return dataResourceEntity;
            }
        }
        return null;
    }

    @Override
    public List<DataResource> getResources() {
        List<DataResource> dataResources = new ArrayList<>();
        dataResources.addAll(resources);
        return dataResources;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Data that = (Data) o;

        return getId() == null ? that.getId() == null : getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", contributorId=" + contributorId +
                ", visibility=" + visibility +
                ", accessibility=" + accessibility +
                ", releasedDate=" + releasedDate +
                ", resources=" + resources +
                ", approvedUsers=" + approvedUsers +
                "} " + super.toString();
    }
}
