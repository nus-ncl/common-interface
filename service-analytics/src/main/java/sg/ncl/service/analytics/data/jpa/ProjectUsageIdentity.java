package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ProjectUsageIdentity implements Serializable {

    @NotNull
    @Column(name = "project_details_id")
    private Long projectDetailsId;

    @NotNull
    @Column(name = "month_year")
    private String monthYear;

    @Override
    public String toString() {
        return "ProjectUsageIdentity{" +
                "projectDetailsId=" + projectDetailsId +
                ", month=" + monthYear +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProjectUsageIdentity that = (ProjectUsageIdentity) o;
        return (projectDetailsId.equals(that.getProjectDetailsId()) && monthYear.equals(that.getMonthYear()));
    }

    @Override
    public int hashCode() {
        return (projectDetailsId.hashCode() + monthYear.hashCode());
    }
}
