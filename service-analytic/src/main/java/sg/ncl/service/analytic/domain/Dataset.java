package sg.ncl.service.analytic.domain;

/**
 * @author: Tran Ly Vu
 */

public interface Dataset {


    /**
     * Returns the id of this {@link Dataset}.
     *
     * @return the id of this {@link Dataset}.
     */
    Long getId();

    /**
     * Returns the name of this {@link Dataset}.
     *
     * @return the name of this {@link Dataset}.
     */
    String getName();

    /**
     * Returns the description of this {@link Dataset}.
     *
     * @return the description of this {@link Dataset}.
     */
    String getDescription();

    /**
     * Returns the accesssibility of this {@link Dataset}.
     *
     * @return the accesssibility of this {@link Dataset}.
     */
    String getAccessibility();

    /**
     * Returns the visibility of this {@link Dataset}.
     *
     * @return the visibility of this {@link Dataset}.
     */
    String getVisibility();


    /**
     * Returns the contributor id of this {@link Dataset}.
     *
     * @return the ontributor idof this {@link Dataset}.
     */
    String getContributorId();


}

