package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jng on 17/10/16.
 */
@Getter
public class DataInfo implements Data {

    private Long id;
    private String name;
    private String description;
    private String contributorId;
    private DataVisibility visibility;
    private DataAccessibility accessibility;
    private List<DataResource> resources;
    private List<String> approvedUsers;

    @JsonCreator
    public DataInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("contributorId") final String contributorId,
            @JsonProperty("visibility") final DataVisibility visibility,
            @JsonProperty("accessibility") final DataAccessibility accessibility,
            @JsonProperty("resources") final List<? extends DataResource> resources,
            @JsonProperty("approvedUsers") final List<String> approvedUsers
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contributorId = contributorId;
        this.visibility = visibility;
        this.accessibility = accessibility;
        this.resources = resources.stream().map(DataResourceInfo::new).collect(Collectors.toList());
        this.approvedUsers = approvedUsers;
    }

    public DataInfo(final Data data) {
        this(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getContributorId(),
                data.getVisibility(),
                data.getAccessibility(),
                data.getResources(),
                data.getApprovedUsers()
        );
    }
}
