package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;
import lombok.Setter;
import sg.ncl.common.jpa.AbstractEntity;
import sg.ncl.service.analytics.domain.ProjectUsage;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "project_usage")
public class ProjectUsageEntity extends AbstractEntity implements ProjectUsage {

    @EmbeddedId
    private ProjectUsageIdentity id;

    @Column(name = "monthly_usage")
    private Integer monthlyUsage;

    @ManyToOne
    @JoinColumn(name = "project_details_id", insertable = false, updatable = false)
    private ProjectDetailsEntity projectDetailsEntity;

    @Override
    public String toString() {
        return "ProjectUsageEntity{" +
                "id=" + id.toString() +
                ", monthlyUsage='" + monthlyUsage +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProjectUsageEntity that = (ProjectUsageEntity) o;
        return (id.equals(that.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
