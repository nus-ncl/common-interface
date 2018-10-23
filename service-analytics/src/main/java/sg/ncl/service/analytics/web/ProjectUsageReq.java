package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;

@Getter
public class ProjectUsageReq {

    private Long projectDetailsId;
    private String month;
    private Integer usage;

    @JsonCreator
    public ProjectUsageReq(
            @JsonProperty("projectDetailsId") final Long projectDetailsId,
            @JsonProperty("month") final String month,
            @JsonProperty("usage") final Integer usage
    ) {
        this.projectDetailsId = projectDetailsId;
        this.month = month;
        this.usage = usage;
    }

    public ProjectUsageIdentity getIdentity() {
        ProjectUsageIdentity identity = new ProjectUsageIdentity();
        identity.setProjectDetailsId(projectDetailsId);
        identity.setMonthYear(month);
        return identity;
    }
}
