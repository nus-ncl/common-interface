package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.domain.NodesReserved;

import java.time.ZonedDateTime;

@Getter
public class NodesReservedInfo implements NodesReserved {

    private final String teamId;
    private final Integer numNodes;
    private final ZonedDateTime startDate;
    private final ZonedDateTime endDate;

    @JsonCreator
    public NodesReservedInfo(
            @JsonProperty("teamId") final String teamId,
            @JsonProperty("numNodes") final int numNodes,
            @JsonProperty("startDate") final ZonedDateTime startDate,
            @JsonProperty("endDate") final ZonedDateTime endDate) {
        this.teamId = teamId;
        this.numNodes = numNodes;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public NodesReservedInfo(final NodesReserved nodesReserved) {
        this(
                nodesReserved.getTeamId(),
                nodesReserved.getNumNodes(),
                nodesReserved.getStartDate(),
                nodesReserved.getEndDate()
        );
    }
}
