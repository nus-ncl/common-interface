package sg.ncl.service.analytics.domain;

import sg.ncl.service.analytics.data.jpa.NodeUsageEntry;
import sg.ncl.service.analytics.data.jpa.NodesReservationEntity;
import sg.ncl.service.analytics.data.jpa.NodesReservationEntry;
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;
import sg.ncl.service.analytics.web.NodesReservationInfo;

import java.time.ZonedDateTime;
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

    NodesReservation applyNodesReserve(Long projectId, NodesReserved nodesRes);

    List<NodesReservationEntry> getNodesReserve(ZonedDateTime startDate, ZonedDateTime endDate);

    List<NodeUsageEntry> getNodesReserveByProject(Long projectId, ZonedDateTime currentDate);

    NodesReservation editNodesReserve(Long reservationId, NodesReservationInfo nodesRes);

    NodesReservationEntity getNodeReservationDetails(Long id);

    NodesReservation deleteNodesReserve(Long reservationId);
}
