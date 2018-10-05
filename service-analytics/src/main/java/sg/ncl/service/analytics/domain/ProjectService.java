package sg.ncl.service.analytics.domain;

import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;

import java.util.List;

public interface ProjectService {

    ProjectDetails getProjectDetails(Long id);

    List<ProjectDetails> getAllProjectDetails();

    ProjectDetails createProjectDetails(ProjectDetails projectDetails);

    ProjectDetails updateProjectDetails(Long id, ProjectDetails projectDetails);

    ProjectDetails deleteProjectDetails(Long id);

    ProjectDetails createProjectUsage(Long id, ProjectUsage projectUsage);

    ProjectDetails updateProjectUsage(Long id, ProjectUsage projectUsage);

    ProjectDetails deleteProjectUsage(Long id, ProjectUsageIdentity identity);

    ProjectUsage findProjectUsageById(Long id, ProjectUsageIdentity identity);

}
