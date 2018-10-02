package sg.ncl.service.analytics.domain;

import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;

public interface ProjectUsage {

    ProjectUsageIdentity getId();

    Integer getMonthlyUsage();
}
