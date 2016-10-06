package sg.ncl.service.data.domain;

import java.util.List;

/**
 * Created by dcszwang on 10/5/2016.
 */
public interface Dataset {

    String getId();

    String getName();

    String getDescription();

    String getOwnerId();

    DatasetVisibility getVisibility();

    DatasetAccessibility getAccessibility();

    DatasetStatus getStatus();

    List<? extends DatasetResource> getResources();

    List<String> getApprovedUsers();
}
