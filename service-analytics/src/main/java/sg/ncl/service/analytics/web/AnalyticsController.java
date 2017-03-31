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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static sg.ncl.common.validation.Validator.isAdmin;

/**
 * @author: Tran Ly Vu, James Ng
 * @version: 1.0
 *
 * References:
 * [1] http://stackoverflow.com/questions/12296642/is-it-possible-to-have-empty-requestparam-values-use-the-defaultvalue
 * [2] http://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/spring-mvc-request-param/
 * [3] https://blog.stackhunter.com/2014/11/14/new-date-time-apis-java-8/
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

    @GetMapping("/usage/teams/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getUsageStatistics(@AuthenticationPrincipal Object claims,
                                     @PathVariable final String id,
                                     @RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam(value = "endDate", required = false) String endDate) {
        if (claims == null || !(claims instanceof Claims)) {
            log.warn("Access denied for: /analytics/usage/teams GET");
            throw new UnauthorizedException();
        }

        ZonedDateTime start = getZonedDateTime(startDate);
        ZonedDateTime end = getZonedDateTime(endDate);
        ZonedDateTime now = ZonedDateTime.now();
        if (start == null)
            start = now.with(firstDayOfMonth());
        if (end == null)
            end = now.with(lastDayOfMonth());

        return analyticsService.getUsageStatistics(id, start, end);
    }

    @GetMapping("/energy")
    @ResponseStatus(HttpStatus.OK)
    public List<Double> getEnergyStatistics(@AuthenticationPrincipal Object claims,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        @RequestParam(value = "endDate", required = false) String endDate) {

        //check admin using validator class from common
        //isAdmin((Claims) claims);

        ZonedDateTime start = getZonedDateTime(startDate);
        ZonedDateTime end = getZonedDateTime(endDate);
        ZonedDateTime now = ZonedDateTime.now();
        if (start == null) {
            start = now.with(firstDayOfMonth());
        }
        if (end == null) {
            end = now.with(lastDayOfMonth());
        }

        return analyticsService.getEnergyStatistics(start, end);
    }

}
