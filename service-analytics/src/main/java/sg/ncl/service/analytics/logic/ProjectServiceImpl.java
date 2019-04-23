package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.analytics.data.jpa.*;
import sg.ncl.service.analytics.domain.*;
import sg.ncl.service.analytics.exceptions.*;
import sg.ncl.service.analytics.web.NodesReservationInfo;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private static final String INFO_TEXT = "Project details saved: {}";
    private static final String WARN_TEXT = "Project usage not found.";

    private final ProjectDetailsRepository projectDetailsRepository;
    private final NodesReservationRepository nodesReservationRepository;

    public ProjectServiceImpl() {
        projectDetailsRepository = null;
        nodesReservationRepository=null;
    }

    @Inject
    ProjectServiceImpl(@NotNull ProjectDetailsRepository projectDetailsRepository, NodesReservationRepository nodesReservationRepository) {
        this.projectDetailsRepository = projectDetailsRepository;
        this.nodesReservationRepository = nodesReservationRepository;
    }

    private ProjectDetailsEntity setUpProjectDetailsEntity(ProjectDetails projectDetails, ProjectDetailsEntity... projectDetailsEntities) {
        ProjectDetailsEntity detailsEntity;

        if (projectDetailsEntities.length > 0) {
            detailsEntity = projectDetailsEntities[0];
        } else {
            detailsEntity = new ProjectDetailsEntity();
        }

        detailsEntity.setOrganisationType(projectDetails.getOrganisationType());
        detailsEntity.setOrganisationName(projectDetails.getOrganisationName());
        detailsEntity.setProjectName(projectDetails.getProjectName());
        detailsEntity.setOwner(projectDetails.getOwner());
        detailsEntity.setDateCreated(projectDetails.getDateCreated());
        detailsEntity.setEducation(projectDetails.isEducation());
        detailsEntity.setServiceTool(projectDetails.isServiceTool());
        detailsEntity.setSupportedBy(projectDetails.getSupportedBy());

        return detailsEntity;
    }

    private ProjectUsageEntity setUpProjectUsageEntity(ProjectUsage projectUsage) {
        ProjectUsageEntity usageEntity = new ProjectUsageEntity();
        usageEntity.setId(projectUsage.getId());
        usageEntity.setMonthlyUsage(projectUsage.getMonthlyUsage());
        return usageEntity;
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
                    throw new ProjectNameAlreadyExistsException("Project name is in use: " + projectDetails.getProjectName());
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

    @Transactional
    @Override
    public ProjectDetails createProjectUsage(Long id, ProjectUsage projectUsage) {
        ProjectDetailsEntity detailsEntity = (ProjectDetailsEntity) getProjectDetails(id);
        List<ProjectUsage> usageEntities = detailsEntity.getProjectUsages();

        if (usageEntities != null) {
            for (ProjectUsage usageEntity : usageEntities) {
                if (usageEntity.getId().equals(projectUsage.getId())) {
                    log.warn("Project usage already in use: {}", projectUsage.getId());
                    throw new ProjectUsageAlreadyExistsException("Project usage already in use: " + projectUsage.getId());
                }
            }
        }

        detailsEntity.addProjectUsage(setUpProjectUsageEntity(projectUsage));
        ProjectDetailsEntity savedDetailsEntity = projectDetailsRepository.save(detailsEntity);
        log.info(INFO_TEXT, savedDetailsEntity);
        return savedDetailsEntity;
    }

    @Transactional
    @Override
    public ProjectDetails updateProjectUsage(Long id, ProjectUsage projectUsage) {
        ProjectDetailsEntity detailsEntity = (ProjectDetailsEntity) getProjectDetails(id);
        ProjectUsage updatedProjectUsage = detailsEntity.updateProjectUsage(projectUsage);

        if (updatedProjectUsage != null) {
            log.info("Project usage updated: {}", updatedProjectUsage);
        } else {
            log.warn("Project usage cannot be found.");
            throw new ProjectUsageNotFoundException("Project usage cannot be found.");
        }

        ProjectDetailsEntity savedDetailsEntity = projectDetailsRepository.save(detailsEntity);
        log.info(INFO_TEXT, savedDetailsEntity);
        return savedDetailsEntity;
    }

    @Transactional
    @Override
    public ProjectDetails deleteProjectUsage(Long id, ProjectUsageIdentity usageIdentity) {
        ProjectDetailsEntity detailsEntity = (ProjectDetailsEntity) getProjectDetails(id);
        ProjectUsage projectUsage = findProjectUsageById(id, usageIdentity);

        if (projectUsage != null) {
            detailsEntity.removeProjectUsage((ProjectUsageEntity) projectUsage);
            log.info("Project usage deleted: {}", projectUsage);
        }

        ProjectDetailsEntity savedDetailsEntity = projectDetailsRepository.save(detailsEntity);
        log.info(INFO_TEXT, savedDetailsEntity);
        return savedDetailsEntity;
    }

    @Override
    public ProjectUsage findProjectUsageById(Long id, ProjectUsageIdentity usageIdentity) {
        ProjectDetailsEntity detailsEntity = (ProjectDetailsEntity) getProjectDetails(id);
        List<ProjectUsage> projectUsages = detailsEntity.getProjectUsages();
        ProjectUsage projectUsage = projectUsages.stream().filter(o -> o.getId().equals(usageIdentity)).findFirst().orElse(null);
        if (projectUsage == null) {
            log.warn(WARN_TEXT);
            throw new ProjectUsageNotFoundException(WARN_TEXT);
        }
        return projectUsage;
    }

    /**
     * Reserve a specified number of nodes for a team in calender
     * from the given start and end date
     * This is not physical reservation of nodes in deterlab,
     * this reservation is only for keeping reservation status for records
     *   }
     */
    @Override
    @Transactional
    public NodesReservation applyNodesReserve(Long projectId, NodesReserved nodesRes, String requesterId){
        // check that the start date is before the end date
        if (nodesRes.getStartDate().isAfter(nodesRes.getEndDate()))
            throw new StartDateAfterEndDateException();

        // check that this project already exist if yes then make reservation
        if (getProjectDetails(projectId) == null) {
            log.warn("applyNodesReserve error: project {} not found", projectId);
            throw new ProjectDetailsNotFoundException("Project details not found.");
        }

        // check that nodes reservation does not overlapped existing reservations of the same project
        if (nodesReservationRepository.existsByOverlappedDates(projectId, nodesRes.getStartDate(), nodesRes.getEndDate())) {
            log.warn("applyNodesReserve error: project {} already has reservations within the period.", projectId);
            throw new NodesReservationAlreadyExistsException("Project already has reservations within the period.");
        }

        // reserve nodes in new table - Nodes Reservation//
        NodesReservationEntity nodesReservationEntity = new NodesReservationEntity();
        nodesReservationEntity.setProjectId(projectId);
        nodesReservationEntity.setStartDate(nodesRes.getStartDate());
        nodesReservationEntity.setEndDate(nodesRes.getEndDate());
        nodesReservationEntity.setNoNodes(nodesRes.getNumNodes());
        final NodesReservation savedTeamNodesReservation = nodesReservationRepository.save(nodesReservationEntity);
        log.info("Nodes Reservation done for the team: {}", savedTeamNodesReservation.getId());
        return savedTeamNodesReservation ;
    }

    @Override
    public List<NodesReservationEntry> getNodesReserve(ZonedDateTime startDate, ZonedDateTime endDate) {
        // check that the start date is before the end date
        if (startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException();

        return nodesReservationRepository.findNodesReservationOverlappedDates(startDate, endDate);
    }



    @Override
    @Transactional
    public List<NodeUsageInfo> getProjNodesUsageInfo(Long projectId,ZonedDateTime currentDate, String requesterId){

        final List<NodeUsageInfo> nodeReservationList = nodesReservationRepository.getProjNodesUsageInfo(projectId,currentDate);
        return nodeReservationList ;
    }

    @Override
    @Transactional
    public NodesReservation editNodesReserve(Long reservationId, NodesReservationInfo nodesRes, String requesterId){

        // check that the start date is before the end date
        if (nodesRes.getStartDate().isAfter(nodesRes.getEndDate()))
            throw new StartDateAfterEndDateException();

        // check that this reservation already exist if yes then make reservation
        NodesReservationEntity nodesReservationEntity = getNodeReservationDetails(reservationId);

        if (nodesReservationEntity == null) {
            log.warn("editNodesReserve error: reservation {} not found", reservationId);
            throw new NodesReservationNotFoundException("Node Reservation details not found.");
        }

        else
        {

            // check that nodes reservation does not overlapped existing reservations of the same project
            if (nodesReservationRepository.existsByOverlappedDatesNotSameReservId(nodesRes.getProjectId(),reservationId, nodesRes.getStartDate(), nodesRes.getEndDate())) {
                log.warn("editNodesReserve error: project {} already has other reservations within the period.", nodesRes.getProjectId());
                throw new NodesReservationAlreadyExistsException("Project already has other reservations within the period.");
            }
            // edit nodes reservation - Nodes Reservation//

            nodesReservationEntity.setStartDate(nodesRes.getStartDate());
            nodesReservationEntity.setEndDate(nodesRes.getEndDate());
            nodesReservationEntity.setNoNodes(nodesRes.getNoNodes());
            final NodesReservation savedNodesReservation = nodesReservationRepository.save(nodesReservationEntity);
            log.info("Nodes Reservation edited for the project: ", savedNodesReservation.getProjectId());
            return savedNodesReservation ;
        }

    }

    @Override
    public NodesReservationEntity getNodeReservationDetails(Long id) {
        NodesReservationEntity nodesReservationEntity = nodesReservationRepository.getOne(id);
        if (nodesReservationEntity == null) {
            throw new NodesReservationNotFoundException("Node Reservation details not found.");
        }
        return  nodesReservationEntity;
    }
}
