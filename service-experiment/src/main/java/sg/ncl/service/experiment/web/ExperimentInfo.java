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
    private String teamName;
    private String name;
    private String description;
    private String nsFile;
    private String nsFileContent;
    private Integer idleSwap;
    private Integer maxDuration;

    public ExperimentInfo(final Long id,
                          final String userId,
                          final String teamId,
                          final String teamName,
                          final String name,
                          final String description,
                          final String nsFile,
                          final String nsFileContent,
                          final Integer idleSwap,
                          final Integer maxDuration) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.name = name;
        this.description = description;
        this.nsFile = nsFile;
        this.nsFileContent = nsFileContent;
        this.idleSwap = idleSwap;
        this.maxDuration = maxDuration;
    }

    public ExperimentInfo(final Experiment experiment) {
        this(experiment.getId(),
                experiment.getUserId(),
                experiment.getTeamId(),
                experiment.getTeamName(),
                experiment.getName(),
                experiment.getDescription(),
                experiment.getNsFile(),
                experiment.getNsFileContent(),
                experiment.getIdleSwap(),
                experiment.getMaxDuration());
    }

    public ExperimentInfo(final ExperimentEntity experimentEntity) {
        this(experimentEntity.getId(),
                experimentEntity.getUserId(),
                experimentEntity.getTeamId(),
                experimentEntity.getTeamName(),
                experimentEntity.getName(),
                experimentEntity.getDescription(),
                experimentEntity.getNsFile(),
                experimentEntity.getNsFileContent(),
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
    public String getTeamName() {
        return teamName;
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
    public String getNsFileContent() {
        return nsFileContent;
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
