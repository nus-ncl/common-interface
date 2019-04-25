package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

public interface NodesReserved {

    String getTeamId();

    ZonedDateTime getStartDate();

    ZonedDateTime getEndDate();

    Integer getNumNodes();
}
