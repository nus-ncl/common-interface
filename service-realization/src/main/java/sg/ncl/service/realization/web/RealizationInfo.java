package sg.ncl.service.realization.web;

import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.realization.domain.RealizationState;

/**
 * Created by Desmond.
 */
public class RealizationInfo implements Realization {

    private Long id;
    private Long experimentId;
    private String experimentName;
    private String userId;
    private String teamId;
    private Integer numberOfNodes;
    private RealizationState state;
    private Long idleMinutes;
    private Long runningMinutes;

    public RealizationInfo(final Long id,
                           final Long experimentId,
                           final String experimentName,
                           final String userId,
                           final String teamId,
                           final Integer numberOfNodes,
                           final RealizationState state,
                           final Long idleMinutes,
                           final Long runningMinutes) {

        this.id = id;
        this.experimentId = experimentId;
        this.experimentName = experimentName;
        this.userId = userId;
        this.teamId = teamId;
        this.numberOfNodes = numberOfNodes;
        this.state = state;
        this.idleMinutes = idleMinutes;
        this.runningMinutes = runningMinutes;
    }

    public RealizationInfo(final RealizationEntity realizationEntity) {
        this(realizationEntity.getId(),
                realizationEntity.getExperimentId(),
                realizationEntity.getExperimentName(),
                realizationEntity.getUserId(),
                realizationEntity.getTeamId(),
                realizationEntity.getNumberOfNodes(),
                realizationEntity.getState(),
                realizationEntity.getIdleMinutes(),
                realizationEntity.getRunningMinutes());
    }

    public RealizationInfo(final Realization realization) {
        this(realization.getId(),
                realization.getExperimentId(),
                realization.getExperimentName(),
                realization.getUserId(),
                realization.getTeamId(),
                realization.getNumberOfNodes(),
                realization.getState(),
                realization.getIdleMinutes(),
                realization.getRunningMinutes());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getExperimentId() {
        return experimentId;
    }

    @Override
    public String getExperimentName() {
        return experimentName;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getTeamId() {
        return teamId;
    }

    @Override
    public Integer getNumberOfNodes() {
        return numberOfNodes;
    }

    @Override
    public RealizationState getState() {
        return state;
    }

    @Override
    public Long getIdleMinutes() {
        return idleMinutes;
    }

    @Override
    public Long getRunningMinutes() {
        return runningMinutes;
    }
}
