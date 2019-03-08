package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.domain.NodesReserv;


import java.time.ZonedDateTime;
@Getter
public class NodesReservationInfo implements NodesReserv {

    private final String teamId;
    private final Integer numNodes;
    private final  String startDate;
    private final String endDate;

    @JsonCreator
    public NodesReservationInfo(
           @JsonProperty("teamId") final String teamId,
            @JsonProperty("numNodes") final int numNodes,
            @JsonProperty("startDate") final String startDate,
            @JsonProperty("endDate") final String endDate) {
        this.teamId = teamId;
        this.numNodes = numNodes;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public NodesReservationInfo(final NodesReserv nodesReserv) {
        this(
                nodesReserv.getTeamId(),
                nodesReserv.getNumNodes(),
                nodesReserv.getStartDate(),
                nodesReserv.getEndDate());
    }
}
