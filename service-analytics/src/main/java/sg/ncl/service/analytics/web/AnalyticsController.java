package sg.ncl.service.analytics.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.analytics.domain.AnalyticsService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: Tran Ly Vu
 */

@RestController
@RequestMapping(path = AnalyticsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AnalyticsController {

    static final String PATH = "/analytics";

    private final AnalyticsService analyticsService;

    @GetMapping("/usage/team/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getUsageStatistics(@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate, @PathVariable final String id) {
        if (endDate == null) {
            endDate =  ZonedDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/uu"));
        }
        return analyticsService.getUsageStatistics(id, startDate, endDate);

    }

}
