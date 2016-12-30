package sg.ncl.service.analytics.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.exceptions.StartDateAfterEndDateException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */

@RestController
@RequestMapping(path = AnalyticsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AnalyticsController {

    static final String PATH = "/analytics";

    private final AnalyticsService analyticsService;

    @Inject
    AnalyticsController(@NotNull final AnalyticsService  analyticsService) {
        this. analyticsService =  analyticsService;
    }

    @GetMapping("/usage/team/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getUsageStatistics(@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate, @PathVariable final String id) {

        if (endDate == null) {
            endDate = ZonedDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/uu"));
        }

        if (startDate == null) {
            startDate = endDate.substring(0, 3) + "01" + endDate.substring(5);
        }

        compareDate(startDate, endDate);

        return analyticsService.getUsageStatistics(id, startDate, endDate);

    }

    private void compareDate (String startDate, String endDate) {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        try {

            Date start = format.parse(startDate);
            Date end = format.parse(endDate);

            if (start.compareTo(end) > 0) {
                log.warn("Start date is after end date");
                throw new StartDateAfterEndDateException();
            }

        } catch (ParseException e) {
            log.warn("Error in parsing startDate or endDate");
            e.printStackTrace();
        }

    }

}
