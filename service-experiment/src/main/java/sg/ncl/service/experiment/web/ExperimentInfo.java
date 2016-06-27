package sg.ncl.service.experiment.web;

import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.domain.Experiment;

/**
 * Created by Desmond.
 */
public class ExperimentInfo implements Experiment {

    private Long id;
    private String userId;
    private String teamId;
    private String name;
    private String description;
    private String nsFile;
    private Integer idleSwap;
    private Integer maxDuration;

    public ExperimentInfo(final Long id, final String userId, final String teamId, final String name, final String description, final String nsFile, final Integer idleSwap, final Integer maxDuration) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        this.nsFile = nsFile;
        this.idleSwap = idleSwap;
        this.maxDuration = maxDuration;
    }

    public ExperimentInfo(final ExperimentEntity experimentEntity) {
        this(experimentEntity.getId(),
                experimentEntity.getUserId(),
                experimentEntity.getTeamId(),
                experimentEntity.getName(),
                experimentEntity.getDescription(),
                experimentEntity.getNsFile(),
                experimentEntity.getIdleSwap(),
                experimentEntity.getMaxDuration());
    }

    @Override
    public Long getId() {
        return id;
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
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getNsFile() {
        return nsFile;
    }

    @Override
    public Integer getIdleSwap() {
        return idleSwap;
    }

    @Override
    public Integer getMaxDuration() {
        return maxDuration;
    }


}
