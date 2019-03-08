package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

public interface NodesReservation {

    Long getId();

    Long getProjectId();

    ZonedDateTime getStartDate();

    ZonedDateTime getEndDate();

    Integer getNoNodes();
}
