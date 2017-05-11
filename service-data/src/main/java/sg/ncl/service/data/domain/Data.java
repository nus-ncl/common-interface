package sg.ncl.service.data.domain;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by dcszwang on 10/5/2016.
 */
public interface Data {

    Long getId();

    String getName();

    String getDescription();

    String getContributorId();

    DataVisibility getVisibility();

    DataAccessibility getAccessibility();

    List<DataResource> getResources();

    List<String> getKeywords();

    List<String> getApprovedUsers();

    ZonedDateTime getReleasedDate();

    DataCategory getCategory();

}
