package sg.ncl.service.realization.domain;

/**
 * The {@link Realization} interface represents a realization.
 *
 * @author Christopher Zhong
 */
public interface Realization {

    /**
     * Returns the unique identifier of this {@link Realization}.
     *
     * @return the unique identifier of this {@link Realization}.
     */
    Long getId();
    Long getExperimentId();
    String getExperimentName();
    String getUserId();
    String getTeamId();
    Integer getNumberOfNodes();
    RealizationState getState();
    Long getIdleMinutes();
    Long getRunningMinutes();
    String getDetails();
}
