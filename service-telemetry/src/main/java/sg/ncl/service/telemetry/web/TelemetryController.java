package sg.ncl.service.telemetry.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.analytics.data.jpa.NodeUsageEntry;
import sg.ncl.service.analytics.web.NodesReservationInfo;
import sg.ncl.service.analytics.web.NodesReservedInfo;
import sg.ncl.service.analytics.web.ProjectDetailsInfo;
import sg.ncl.service.telemetry.domain.NodeType;
import sg.ncl.service.telemetry.domain.TelemetryService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import sg.ncl.service.analytics.domain.*;


import static sg.ncl.common.validation.Validator.checkAdmin;
import static sg.ncl.common.validation.Validator.checkClaimsType;
import static sg.ncl.service.telemetry.web.TelemetryController.PATH;

/**
 * Created by dcsyeoty on 16-Dec-16.
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TelemetryController {

    static final String PATH = "/telemetry";

    private final TelemetryService telemetryService;
    private final ProjectService projectService;

    @Inject
    TelemetryController(@NotNull final TelemetryService telemetryService,@NotNull final ProjectService projectService) {
        this.telemetryService = telemetryService;
        this.projectService = projectService;
    }

    @GetMapping(path = "/nodes/counts", params = {"type"})
    @ResponseStatus(HttpStatus.OK)
    public Map<NodeType, String> getNodes(@RequestParam("type") final NodeType nodeType) {
        EnumMap<NodeType, String> map = new EnumMap<>(NodeType.class);
        map.put(nodeType, telemetryService.getNodes(nodeType));
        return map;
    }

    @GetMapping(path = "/nodes/status")
    @ResponseStatus(HttpStatus.OK)
    public String getNodesStatus() {
        return telemetryService.getNodesStatus();
    }

    /**
     * Retrieves the number of logged in users and running experiments at the current time
     * @return a json in the format { users: X, experiments: X }
     */
    @GetMapping(path = "/testbed/stats")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getTestbedStats() {
        Map<String, String> map = new HashMap<>();
        map.put("users", telemetryService.getLoggedInUsersCount());
        map.put("experiments", telemetryService.getRunningExperimentsCount());
        return map;
    }

    @GetMapping("/usage/projects")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDetails> getAllProjectDetails() {
        return projectService.getAllProjectDetails().stream().map(ProjectDetailsInfo::new).collect(Collectors.toList());
    }
    // for Nodes Reservation/Booking in advance
    @PostMapping(path = "/usage/projects/{id}/nodesreservations")
    @ResponseStatus(HttpStatus.OK)
    public NodesReservation applyNodesReserve(@PathVariable Long id,
                                              @RequestBody @Valid NodesReservedInfo nodesResInfo) {
        return projectService.applyNodesReserve(id, nodesResInfo);
    }

    @PostMapping(path = "/usage/nodesreservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NodesReservation editNodesReserve(@PathVariable Long id,
                                             @RequestBody @Valid NodesReservationInfo nodesResInfo) {
        return projectService.editNodesReserve(id, nodesResInfo);
    }

    @GetMapping(path = "/usage/projects/{id}/nodesreservations")
    @ResponseStatus(HttpStatus.OK)
    public Map<Long, List<String>> getNodesReserveByProject(@PathVariable Long id) {
        ZonedDateTime now = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<NodeUsageEntry> lstNodeUsageEntry = projectService.getNodesReserveByProject(id, now);
        Map<Long, List<String>> mapNodeReservationInfo = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (NodeUsageEntry entry : lstNodeUsageEntry) {
            List<String> tmpList = new ArrayList<>();
            tmpList.add(entry.getStartDate().format(formatter));
            tmpList.add(entry.getEndDate().format(formatter));
            tmpList.add(entry.getNoNodes().toString());
            mapNodeReservationInfo.put(entry.getId(), tmpList);
        }
        return mapNodeReservationInfo;
    }

    @DeleteMapping(path = "/usage/nodesreservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NodesReservation removeNodesReserve(@PathVariable Long id) {
        return new NodesReservationInfo(projectService.deleteNodesReserve(id));
    }
}
