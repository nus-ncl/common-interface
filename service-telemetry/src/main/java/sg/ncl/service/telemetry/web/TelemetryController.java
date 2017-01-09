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
}
