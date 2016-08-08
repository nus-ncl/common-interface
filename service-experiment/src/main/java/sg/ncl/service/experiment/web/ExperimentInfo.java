package sg.ncl.service.experiment.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.experiment.domain.Experiment;

/**
 * Created by Desmond.
 */
@Getter
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

    @JsonCreator
    public ExperimentInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("userId") final String userId,
            @JsonProperty("teamId") final String teamId,
            @JsonProperty("teamName") final String teamName,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("nsFile") final String nsFile,
            @JsonProperty("nsFileContent") final String nsFileContent,
            @JsonProperty("idleSwap") final Integer idleSwap,
            @JsonProperty("maxDuration") final Integer maxDuration
    ) {
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
        this(
                experiment.getId(),
                experiment.getUserId(),
                experiment.getTeamId(),
                experiment.getTeamName(),
                experiment.getName(),
                experiment.getDescription(),
                experiment.getNsFile(),
                experiment.getNsFileContent(),
                experiment.getIdleSwap(),
                experiment.getMaxDuration()
        );
    }

}
