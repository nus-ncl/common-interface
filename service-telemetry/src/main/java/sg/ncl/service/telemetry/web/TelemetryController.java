package sg.ncl.service.telemetry.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.telemetry.domain.TelemetryService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getFreeNodes() {
        Map<String, String> map = new HashMap<>();
        map.put("free", telemetryService.getFreeNodes());
        return map;
    }
}