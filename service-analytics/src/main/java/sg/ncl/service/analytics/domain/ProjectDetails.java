package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;
import java.util.List;

public interface ProjectDetails {

    Long getId();

    String getOrganisationType();

    String getOrganisationName();

    String getProjectName();

    String getOwner();

    ZonedDateTime getDateCreated();

    boolean isEducation();

    boolean isServiceTool();

    String getSupportedBy();

    List<ProjectUsage> getProjectUsages();
}
