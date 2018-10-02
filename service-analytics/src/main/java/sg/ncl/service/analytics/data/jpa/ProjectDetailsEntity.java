package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytics.domain.ProjectDetails;
import sg.ncl.service.analytics.domain.ProjectUsage;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "project_details")
public class ProjectDetailsEntity extends AbstractEntity implements ProjectDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "organisation_type", nullable = false)
    private String organisationType;

    @Column(name = "organisation_name", nullable = false)
    private String organisationName;

    @Column(name = "project_name", nullable = false, unique = true)
    private String projectName;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "date_created", nullable = false)
    private ZonedDateTime dateCreated;

    @Column(name = "is_education", nullable = false)
    @Type(type = "yes_no")
    private boolean education = false;

    @Column(name = "is_service_tool", nullable = false)
    @Type(type = "yes_no")
    private boolean serviceTool = false;

    @Type(type = "text")
    @Column(name = "supported_by", nullable = false)
    private String supportedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectDetailsEntity", orphanRemoval = true)
    private List<ProjectUsageEntity> projectUsages = new ArrayList<>();

    @Override
    public List<ProjectUsage> getProjectUsages() {
        return new ArrayList<>(projectUsages);
    }

    public void addProjectUsage(ProjectUsageEntity projectUsageEntity) {
        if (!projectUsages.contains(projectUsageEntity)) {
            projectUsages.add(projectUsageEntity);
            projectUsageEntity.setProjectDetailsEntity(this);
            log.info("Project usage {} added in the list for project details {}", projectUsageEntity, id);
        } else {
            log.info("Project usage {} is already in the list for project details {}", projectUsageEntity, id);
        }
    }

    public void removeProjectUsage(ProjectUsageEntity projectUsageEntity) {
        if (projectUsages.contains(projectUsageEntity)) {
            projectUsages.remove(projectUsageEntity);
            log.info("Project usage {} removed from the list for project details {}", projectUsageEntity, id);
        } else {
            log.info("Project usage {} not in the list for project details {}", projectUsageEntity, id);
        }
    }

    public ProjectUsage updateProjectUsage(ProjectUsage inputProjectUsage) {
        for (ProjectUsageEntity projectUsageEntity : projectUsages) {
            if (projectUsageEntity.getId().equals(inputProjectUsage.getId())) {
                projectUsageEntity.setMonthlyUsage(inputProjectUsage.getMonthlyUsage());
                log.info("Project usage {} is updated for project details {}", projectUsageEntity, id);
                return projectUsageEntity;
            }
        }
        log.info("Project usage {} not in the list for project details {}", inputProjectUsage, id);
        return null;
    }

    @Override
    public String toString() {
        return "ProjectDetailsEntity{" +
                "id='" + id + '\'' +
                ", organisationType=" + organisationType +
                ", organisationName=" + organisationName +
                ", projectName=" + projectName +
                ", owner=" + owner +
                ", dateCreated=" + dateCreated +
                ", education=" + education +
                ", serviceTool=" + serviceTool +
                ", supportedBy=" + supportedBy +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProjectDetailsEntity that = (ProjectDetailsEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
