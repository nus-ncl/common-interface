package sg.ncl.service.experiment.domain;

import java.time.ZonedDateTime;

/**
 * The {@link Experiment} interface represents a realization.
 * <p>
 * Created by Desmond
 */
public interface Experiment {

    /**
     * Returns the unique identifier of this {@link Experiment}.
     *
     * @return the unique identifier of this {@link Experiment}.
     */
    Long getId();

    String getUserId();

    String getTeamId();

    String getTeamName();

    String getName();

    String getDescription();

    String getNsFile();

    String getNsFileContent();

    Integer getIdleSwap();

    Integer getMaxDuration();

    ZonedDateTime getCreatedDate();

    ZonedDateTime getLastModifiedDate();
}

