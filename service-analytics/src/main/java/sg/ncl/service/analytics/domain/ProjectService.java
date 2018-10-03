package sg.ncl.service.analytics.domain;

import java.util.List;

public interface ProjectService {

    ProjectDetails getProjectDetails(Long id);

    List<ProjectDetails> getAllProjectDetails();

    ProjectDetails createProjectDetails(ProjectDetails projectDetails);

    ProjectDetails updateProjectDetails(Long id, ProjectDetails projectDetails);

    ProjectDetails deleteProjectDetails(Long id);

}
