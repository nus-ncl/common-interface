package sg.ncl.service.experiment.domain;

/**
 * The {@link Experiment} interface represents a realization.
 *
 * @author Christopher Zhong
 */
public interface Experiment {

    /**
     * Returns the unique identifier of this {@link Experiment}.
     *
     * @return the unique identifier of this {@link Experiment}.
     */
    Long getId();
    String getTeamId();
    String getName();
    String getDescription();
    String getNsFile();
    Integer getIdleSwap();
    Integer getMaxDuration();
}
