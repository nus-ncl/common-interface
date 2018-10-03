package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.analytics.data.jpa.ProjectDetailsEntity;
import sg.ncl.service.analytics.data.jpa.ProjectDetailsRepository;
import sg.ncl.service.analytics.domain.ProjectDetails;
import sg.ncl.service.analytics.domain.ProjectService;
import sg.ncl.service.analytics.exceptions.ProjectDetailsNotFoundException;
import sg.ncl.service.analytics.exceptions.ProjectNameAlreadyExistsException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private static final String INFO_TEXT = "Project details saved: {}";

    private final ProjectDetailsRepository projectDetailsRepository;

    public ProjectServiceImpl() {
        projectDetailsRepository = null;
    }

    @Inject
    ProjectServiceImpl(@NotNull ProjectDetailsRepository projectDetailsRepository) {
        this.projectDetailsRepository = projectDetailsRepository;
    }

    private ProjectDetailsEntity setUpProjectDetailsEntity(ProjectDetails projectDetails, ProjectDetailsEntity... projectDetailsEntities) {
        ProjectDetailsEntity detailsEntity;

        if (projectDetailsEntities.length > 0) {
            detailsEntity = projectDetailsEntities[0];
        } else {
            detailsEntity = new ProjectDetailsEntity();
            detailsEntity.setDateCreated(projectDetails.getDateCreated());
        }

        detailsEntity.setOrganisationType(projectDetails.getOrganisationType());
        detailsEntity.setOrganisationName(projectDetails.getOrganisationName());
        detailsEntity.setProjectName(projectDetails.getProjectName());
        detailsEntity.setOwner(projectDetails.getOwner());
        detailsEntity.setEducation(projectDetails.isEducation());
        detailsEntity.setServiceTool(projectDetails.isServiceTool());
        detailsEntity.setSupportedBy(projectDetails.getSupportedBy());

        return detailsEntity;
    }

    @Override
    public ProjectDetails getProjectDetails(Long id) {
        ProjectDetails projectDetails = projectDetailsRepository.getOne(id);
        if (projectDetails == null) {
            throw new ProjectDetailsNotFoundException("Project details not found.");
        }
        return  projectDetails;
    }

    @Override
    public List<ProjectDetails> getAllProjectDetails() {
        return new ArrayList<>(projectDetailsRepository.findAll());
    }

    @Transactional
    @Override
    public ProjectDetails createProjectDetails(ProjectDetails projectDetails) {
        log.info("Save project details");

        // check if project name already exists
        List<ProjectDetailsEntity> detailsEntities = projectDetailsRepository.findByProjectName(projectDetails.getProjectName());

        if (detailsEntities != null) {
            for (ProjectDetailsEntity detailsEntity : detailsEntities) {
                if (detailsEntity.getProjectName().equals(projectDetails.getProjectName())) {
                    log.warn("Project name is in use: {}", projectDetails.getProjectName());
                    throw new ProjectNameAlreadyExistsException(("Project name is in use: " + projectDetails.getProjectName()));
                }
            }
        }

        ProjectDetailsEntity savedDetailsEntity = projectDetailsRepository.save(setUpProjectDetailsEntity(projectDetails));
        log.info(INFO_TEXT, savedDetailsEntity);
        return savedDetailsEntity;
    }

    @Transactional
    @Override
    public ProjectDetails updateProjectDetails(Long id, ProjectDetails projectDetails) {
        ProjectDetailsEntity detailsEntity = (ProjectDetailsEntity) getProjectDetails(id);
        ProjectDetailsEntity savedDetailsEntity = projectDetailsRepository.save(setUpProjectDetailsEntity(projectDetails, detailsEntity));
        log.info("Project details updated: {}", savedDetailsEntity);
        return savedDetailsEntity;
    }

    @Transactional
    @Override
    public ProjectDetails deleteProjectDetails(Long id) {
        ProjectDetailsEntity detailsEntity = (ProjectDetailsEntity) getProjectDetails(id);
        projectDetailsRepository.delete(id);
        log.info("Project details deleted: {}", detailsEntity);
        return detailsEntity;
    }
}
