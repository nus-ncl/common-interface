package sg.ncl.service.analytics.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.analytics.domain.ProjectDetails;
import sg.ncl.service.analytics.domain.ProjectUsage;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProjectDetailsInfo implements ProjectDetails {

    private Long id;
    private String organisationType;
    private String organisationName;
    private String projectName;
    private String owner;
    private ZonedDateTime dateCreated;
    private boolean education;
    private boolean serviceTool;
    private String supportedBy;
    private List<ProjectUsage> projectUsages;

    @JsonCreator
    public ProjectDetailsInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("organisationType") final String organisationType,
            @JsonProperty("organisationName") final String organisationName,
            @JsonProperty("projectName") final String projectName,
            @JsonProperty("owner") final String owner,
            @JsonProperty("dateCreated") final ZonedDateTime dateCreated,
            @JsonProperty("is_education") final boolean education,
            @JsonProperty("is_service_tool") final boolean serviceTool,
            @JsonProperty("supportedBy") final String supportedBy,
            @JsonProperty("projectUsages") final List<? extends ProjectUsage> projectUsages
    ) {
        this.id = id;
        this.organisationType = organisationType;
        this.organisationName = organisationName;
        this.projectName = projectName;
        this.owner = owner;
        this.dateCreated = dateCreated;
        this.education = education;
        this.serviceTool = serviceTool;
        this.supportedBy = supportedBy;
        this.projectUsages = projectUsages.stream().map(ProjectUsageInfo::new).collect(Collectors.toList());
    }

    public ProjectDetailsInfo(final ProjectDetails projectDetails) {
        this(
                projectDetails.getId(),
                projectDetails.getOrganisationType(),
                projectDetails.getOrganisationName(),
                projectDetails.getProjectName(),
                projectDetails.getOwner(),
                projectDetails.getDateCreated(),
                projectDetails.isEducation(),
                projectDetails.isServiceTool(),
                projectDetails.getSupportedBy(),
                projectDetails.getProjectUsages()
        );
    }
}
