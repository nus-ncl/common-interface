package sg.ncl.service.realization.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.realization.domain.RealizationState;

/**
 * Created by Desmond.
 */
@Getter
public class RealizationInfo implements Realization {

    private final Long id;
    private final Long experimentId;
    private final String experimentName;
    private final String userId;
    private final String teamId;
    private final Integer numberOfNodes;
    private final RealizationState state;
    private final Long idleMinutes;
    private final Long runningMinutes;
    private final String details;

    @JsonCreator
    public RealizationInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("experimentId") final Long experimentId,
            @JsonProperty("experimentName") final String experimentName,
            @JsonProperty("userId") final String userId,
            @JsonProperty("teamId") final String teamId,
            @JsonProperty("numberOfNodes") final Integer numberOfNodes,
            @JsonProperty("state") final RealizationState state,
            @JsonProperty("idleMinutes") final Long idleMinutes,
            @JsonProperty("runningMinutes") final Long runningMinutes,
            @JsonProperty("details") final String details
    ) {
        this.id = id;
        this.experimentId = experimentId;
        this.experimentName = experimentName;
        this.userId = userId;
        this.teamId = teamId;
        this.numberOfNodes = numberOfNodes;
        this.state = state;
        this.idleMinutes = idleMinutes;
        this.runningMinutes = runningMinutes;
        this.details = details;
    }

    public RealizationInfo(final Realization realization) {
        this(
                realization.getId(),
                realization.getExperimentId(),
                realization.getExperimentName(),
                realization.getUserId(),
                realization.getTeamId(),
                realization.getNumberOfNodes(),
                realization.getState(),
                realization.getIdleMinutes(),
                realization.getRunningMinutes(),
                realization.getDetails()
        );
    }

}
