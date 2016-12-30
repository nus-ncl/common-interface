package sg.ncl.service.analytics.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.exceptions.StartDateAfterEndDateException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author: Tran Ly Vu, James Ng
 * @version: 1.0
 *
 * References:
 * [1] http://stackoverflow.com/questions/12296642/is-it-possible-to-have-empty-requestparam-values-use-the-defaultvalue
 * [2] http://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/spring-mvc-request-param/
 */
@RestController
@RequestMapping(path = AnalyticsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AnalyticsController {

    static final String PATH = "/analytics";

    private final AnalyticsService analyticsService;

    @Inject
    AnalyticsController(@NotNull final AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // Get number of download times for datasets
    // @GetMapping(value = "/datasets/downloads", params = {"id", "startDate", "endDate"})
    @GetMapping("/datasets/downloads")
    @ResponseStatus(HttpStatus.OK)
    public List<DataDownloadStatistics> getDataDownloadCount(@AuthenticationPrincipal Object claims,
                                                             @RequestParam(value = "id", required = false) Long id,
                                                             @RequestParam(value = "startDate", required = false) String startDate,
                                                             @RequestParam(value = "endDate", required = false) String endDate) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /analytics/datasets/downloads GET");
            throw new UnauthorizedException();
        }
        ZonedDateTime start = getZonedDateTime(startDate);
        ZonedDateTime end = getZonedDateTime(endDate);
        return analyticsService.getDataDownloadCount(id, start, end);
    }

    /**
     * Get simple ZonedDateTime from date string in the format 'YYYY-MM-DD'.
     *
     * @param date  date string to convert
     * @return      ZonedDateTime of
     */
    private ZonedDateTime getZonedDateTime(String date) {
        if (date != null) {
            String[] result = date.split("-");
            if (result.length != 3) {
                throw new BadRequestException();
            }
            return ZonedDateTime.of(
                    Integer.parseInt(result[0]),
                    Integer.parseInt(result[1]),
                    Integer.parseInt(result[2]),
                    0, 0, 0, 0, ZoneId.of("Asia/Singapore"));
        }
        return null;
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
            e.printStackTrace();
        }

    }

}
