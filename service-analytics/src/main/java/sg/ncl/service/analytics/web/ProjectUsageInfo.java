package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;
import sg.ncl.service.analytics.domain.ProjectUsage;

@Getter
public class ProjectUsageInfo implements ProjectUsage {

    private ProjectUsageIdentity id;
    private Integer monthlyUsage;

    @JsonCreator
    public ProjectUsageInfo(
            @JsonProperty("id") final ProjectUsageIdentity id,
            @JsonProperty("monthlyUsage") final Integer monthlyUsage
    ) {
        this.id = id;
        this.monthlyUsage = monthlyUsage;
    }

    public ProjectUsageInfo(final ProjectUsage projectUsage) {
        this(projectUsage.getId(), projectUsage.getMonthlyUsage());
    }
}
