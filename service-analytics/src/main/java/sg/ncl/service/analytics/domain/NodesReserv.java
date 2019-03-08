package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

public interface NodesReserv {

    String getTeamId();

    String getStartDate();

    String getEndDate();

    Integer getNumNodes();
}
