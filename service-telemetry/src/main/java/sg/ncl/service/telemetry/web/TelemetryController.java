package sg.ncl.service.telemetry.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.telemetry.domain.NodeType;
import sg.ncl.service.telemetry.domain.TelemetryService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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

    @Inject
    TelemetryController(@NotNull final TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
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
}
