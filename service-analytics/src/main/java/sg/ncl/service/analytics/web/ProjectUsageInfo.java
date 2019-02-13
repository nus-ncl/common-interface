package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;
import sg.ncl.service.analytics.domain.ProjectUsage;

import java.math.BigDecimal;

@Getter
public class ProjectUsageInfo implements ProjectUsage {

    private ProjectUsageIdentity id;
    private Integer monthlyUsage;
    private BigDecimal incurred;
    private BigDecimal waived;

    @JsonCreator
    public ProjectUsageInfo(
            @JsonProperty("id") final ProjectUsageIdentity id,
            @JsonProperty("monthlyUsage") final Integer monthlyUsage,
            @JsonProperty("incurred") final BigDecimal incurred,
            @JsonProperty("waived") final BigDecimal waived
    ) {
        this.id = id;
        this.monthlyUsage = monthlyUsage;
        this.incurred = incurred;
        this.waived = waived;
    }

    public ProjectUsageInfo(final ProjectUsage projectUsage) {
        this(
                projectUsage.getId(),
                projectUsage.getMonthlyUsage(),
                projectUsage.getIncurred(),
                projectUsage.getWaived()
        );
    }
}
