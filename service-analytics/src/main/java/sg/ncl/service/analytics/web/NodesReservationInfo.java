package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.domain.NodesReservation;

import java.time.ZonedDateTime;

@Getter
public class NodesReservationInfo implements NodesReservation {

    private Long id;
    private Long projectId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer noNodes;

    @JsonCreator
    public NodesReservationInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("projectId") final Long projectId,
            @JsonProperty("startDate") final ZonedDateTime startDate,
            @JsonProperty("endDate") final ZonedDateTime endDate,
            @JsonProperty("noNodes") final Integer noNodes
    ) {
        this.id = id;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.noNodes = noNodes;
    }
}
