package sg.ncl.service.analytic.domain;

/**
 * Created by dcsyeoty on 28-Oct-16.
 */
public interface Analytic {

    Long getId();

    String getTeamId();

    String getNodeId();

    String getImageName();

    String getDescription();

    String getCurrentOS();

    ImageVisibility getVisibility();
}
