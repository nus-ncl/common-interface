package sg.ncl.service.experiment.web;

import sg.ncl.service.experiment.domain.Experiment;

/**
 * Created by Desmond.
 */
public class ExperimentInfo implements Experiment {

    private Long id;
    private String teamId;
    private String name;
    private String description;
    private String nsFile;
    private Integer idleSwap;
    private Integer maxDuration;

    @Override
    public Long getId() {
        return id;
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
