package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.*;

import java.time.ZonedDateTime;
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
    private List<String> keywords;
    private List<String> approvedUsers;
    private ZonedDateTime releasedDate;
    private DataCategoryInfo category;

    @JsonCreator
    public DataInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("contributorId") final String contributorId,
            @JsonProperty("visibility") final DataVisibility visibility,
            @JsonProperty("accessibility") final DataAccessibility accessibility,
            @JsonProperty("resources") final List<? extends DataResource> resources,
            @JsonProperty("keywords") final List<String> keywords,
            @JsonProperty("approvedUsers") final List<String> approvedUsers,
            @JsonProperty("releasedDate") final ZonedDateTime releasedDate,
            @JsonProperty("category") final DataCategoryInfo category
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contributorId = contributorId;
        this.visibility = visibility;
        this.accessibility = accessibility;
        this.resources = resources.stream().map(DataResourceInfo::new).collect(Collectors.toList());
        this.keywords = keywords;
        this.approvedUsers = approvedUsers;
        this.releasedDate = releasedDate;
        this.category = category;
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
                data.getKeywords(),
                data.getApprovedUsers(),
                data.getReleasedDate(),
                new DataCategoryInfo(data.getCategory())
        );
    }
}
