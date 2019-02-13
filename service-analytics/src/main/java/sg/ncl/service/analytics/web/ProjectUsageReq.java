package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;

import java.math.BigDecimal;

@Getter
public class ProjectUsageReq {

    private Long projectDetailsId;
    private String month;
    private Integer usage;
    private BigDecimal incurred;
    private BigDecimal waived;

    @JsonCreator
    public ProjectUsageReq(
            @JsonProperty("projectDetailsId") final Long projectDetailsId,
            @JsonProperty("month") final String month,
            @JsonProperty("usage") final Integer usage,
            @JsonProperty("incurred") final BigDecimal incurred,
            @JsonProperty("waived") final BigDecimal waived
    ) {
        this.projectDetailsId = projectDetailsId;
        this.month = month;
        this.usage = usage;
        this.incurred = incurred;
        this.waived = waived;
    }

    public ProjectUsageIdentity getIdentity() {
        ProjectUsageIdentity identity = new ProjectUsageIdentity();
        identity.setProjectDetailsId(projectDetailsId);
        identity.setMonthYear(month);
        return identity;
    }
}
