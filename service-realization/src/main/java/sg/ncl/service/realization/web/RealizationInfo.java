package sg.ncl.service.realization.web;

import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.Realization;

/**
 * Created by Desmond.
 */
public class RealizationInfo implements Realization {

    private Long id;
    private Long experimentId;
    private String userId;
    private String teamId;
    private int numberOfNodes;
    private long idleMinutes;
    private long runningMinutes;

    public RealizationInfo(final Long id, final Long experimentId, final String userId,
                            final String teamId, final int numberOfNodes, final long idleMinutes,
                            final long runningMinutes) {

        this.id = id;
        this.experimentId = experimentId;
        this.userId = userId;
        this.teamId = teamId;
        this.numberOfNodes = numberOfNodes;
        this.idleMinutes = idleMinutes;
        this.runningMinutes = runningMinutes;
    }

    public RealizationInfo(final RealizationEntity realizationEntity) {
        this(realizationEntity.getId(),
                realizationEntity.getExperimentId(),
                realizationEntity.getUserId(),
                realizationEntity.getTeamId(),
                realizationEntity.getNumberOfNodes(),
                realizationEntity.getIdleMinutes(),
                realizationEntity.getRunningMinutes());
    }

    public RealizationInfo(final Realization realization) {
        this(realization.getId(),
                realization.getExperimentId(),
                realization.getUserId(),
                realization.getTeamId(),
                realization.getNumberOfNodes(),
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
    public String getUserId() {
        return userId;
    }

    @Override
    public String getTeamId() {
        return teamId;
    }

    @Override
    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    @Override
    public long getIdleMinutes() {
        return idleMinutes;
    }

    @Override
    public long getRunningMinutes() {
        return runningMinutes;
    }
}
