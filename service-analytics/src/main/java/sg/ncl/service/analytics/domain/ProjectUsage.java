package sg.ncl.service.analytics.domain;

import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;

import java.math.BigDecimal;

public interface ProjectUsage {

    ProjectUsageIdentity getId();

    Integer getMonthlyUsage();

    BigDecimal getIncurred();

    BigDecimal getWaived();
}
