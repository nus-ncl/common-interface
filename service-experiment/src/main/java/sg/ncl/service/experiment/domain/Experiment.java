package sg.ncl.service.experiment.domain;

/**
 * The {@link Experiment} interface represents a realization.
 *
 * Created by Desmond
 */
public interface Experiment {

    /**
     * Returns the unique identifier of this {@link Experiment}.
     *
     * @return the unique identifier of this {@link Experiment}.
     */
    long getId();
    String getUserId();
    String getTeamId();
    String getName();
    String getDescription();
    String getNsFile();
    int getIdleSwap();
    int getMaxDuration();
}

