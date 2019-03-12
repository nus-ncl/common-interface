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
import sg.ncl.service.analytics.data.jpa.ProjectUsageIdentity;
import sg.ncl.service.analytics.domain.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static sg.ncl.common.validation.Validator.checkAdmin;
import static sg.ncl.common.validation.Validator.checkClaimsType;

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
    private final ProjectService projectService;

    @Inject
    AnalyticsController(@NotNull final AnalyticsService analyticsService,
                        @NotNull final ProjectService projectService) {

        this.analyticsService = analyticsService;
        this.projectService = projectService;
    }

    // Get number of download times for datasets by public users
    @GetMapping("/datasets/downloads/public")
    @ResponseStatus(HttpStatus.OK)
    public List<DataDownloadStatistics> getDataPublicDownloadCount(@AuthenticationPrincipal Object claims,
                                                                   @RequestParam(value = "id", required = false) Long id,
                                                                   @RequestParam(value = "startDate", required = false) String startDate,
                                                                   @RequestParam(value = "endDate", required = false) String endDate) {
        if (!(claims instanceof Claims)) {
            log.warn("Access denied for: /analytics/datasets/downloads/public GET");
            throw new UnauthorizedException();
        }
        ZonedDateTime start = getZonedDateTime(startDate);
        ZonedDateTime end = getZonedDateTime(endDate);
        return analyticsService.getDataPublicDownloadCount(id, start, end);
    }

    // Get number of download times for datasets by logged-in users
    @GetMapping("/datasets/downloads")
    @ResponseStatus(HttpStatus.OK)
    public List<DataDownloadStatistics> getDataDownloadCount(@AuthenticationPrincipal Object claims,
                                                             @RequestParam(value = "id", required = false) Long id,
                                                             @RequestParam(value = "startDate", required = false) String startDate,
                                                             @RequestParam(value = "endDate", required = false) String endDate) {
        if (!(claims instanceof Claims)) {
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

    /**
     * Get a team's daily usage for a given period from startDate to endDate (both inclusive)
     * @param id
     * @param startDate e.g., 2018-08-01
     * @param endDate e.g., 2018-08-14
     * @return daily usage in node-minutes
     */
    @GetMapping("/usage/teams/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getUsageStatistics(@AuthenticationPrincipal Object claims,
                                     @PathVariable final String id,
                                     @RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam(value = "endDate", required = false) String endDate) {
        if (!(claims instanceof Claims)) {
            log.warn("Access denied for: /analytics/usage/teams GET");
            throw new UnauthorizedException();
        }

        ZonedDateTime now = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime start = (startDate == null) ? now.with(firstDayOfMonth()) : getZonedDateTime(startDate);
        ZonedDateTime end = (endDate == null) ? now : getZonedDateTime(endDate);

        return analyticsService.getTeamUsage(id, start, end);
    }

    @GetMapping("/energy")
    @ResponseStatus(HttpStatus.OK)
    public List<Double> getEnergyStatistics(@AuthenticationPrincipal Object claims,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        @RequestParam(value = "endDate", required = false) String endDate) {

        //check admin using validator class from common
        checkAdmin((Claims) claims);

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

    @GetMapping("/usage/projects")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDetails> getAllProjectDetails(@AuthenticationPrincipal Object claims) {
        checkAdmin((Claims) claims);
        return projectService.getAllProjectDetails().stream().map(ProjectDetailsInfo::new).collect(Collectors.toList());
    }

    @GetMapping("/usage/projects/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetails getProjectDetailsById(@AuthenticationPrincipal Object claims, @PathVariable Long id) {
        checkAdmin((Claims) claims);
        return new ProjectDetailsInfo(projectService.getProjectDetails(id));
    }

    @PostMapping(path = "/usage/projects", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDetails addProjectDetails(@AuthenticationPrincipal Object claims,
                                            @RequestBody @Valid ProjectDetailsInfo projectDetailsInfo) {
        checkAdmin((Claims) claims);
        return new ProjectDetailsInfo(projectService.createProjectDetails(projectDetailsInfo));
    }

    @PutMapping("/usage/projects/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetails updateProjectDetails(@AuthenticationPrincipal Object claims,
                                               @PathVariable Long id,
                                               @RequestBody @Valid ProjectDetailsInfo projectDetailsInfo) {
        checkAdmin((Claims) claims);
        return new ProjectDetailsInfo(projectService.updateProjectDetails(id, projectDetailsInfo));
    }

    @DeleteMapping("/usage/projects/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String removeProjectDetails(@AuthenticationPrincipal Object claims,
                                       @PathVariable Long id) {
        checkAdmin((Claims) claims);
        return String.valueOf(projectService.deleteProjectDetails(id).getProjectName());
    }

    @GetMapping("/usage/projects/{id}/months/{month}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectUsage getProjectUsage(@AuthenticationPrincipal Object claims,
                                        @PathVariable Long id,
                                        @PathVariable String month) {
        checkAdmin((Claims) claims);
        ProjectUsageIdentity identity = new ProjectUsageIdentity();
        identity.setProjectDetailsId(id);
        identity.setMonthYear(month);
        return new ProjectUsageInfo(projectService.findProjectUsageById(id, identity));
    }

    @PostMapping(path = "/usage/projects/{id}/months", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDetails addProjectUsage(@AuthenticationPrincipal Object claims,
                                          @PathVariable Long id,
                                          @RequestBody @Valid ProjectUsageReq projectUsageReq) {
        checkAdmin((Claims) claims);
        return new ProjectDetailsInfo(projectService.createProjectUsage(
                id, new ProjectUsageInfo(projectUsageReq.getIdentity(), projectUsageReq.getUsage())
        ));
    }

    @PutMapping("/usage/projects/{id}/months")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetails updateProjectUsage(@AuthenticationPrincipal Object claims,
                                             @PathVariable Long id,
                                             @RequestBody @Valid ProjectUsageReq projectUsageReq) {
        checkAdmin((Claims) claims);
        return new ProjectDetailsInfo(projectService.updateProjectUsage(
                id, new ProjectUsageInfo(projectUsageReq.getIdentity(), projectUsageReq.getUsage())
        ));
    }

    @DeleteMapping("/usage/projects/{id}/months/{month}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetails removeProjectUsage(@AuthenticationPrincipal Object claims,
                                             @PathVariable Long id,
                                             @PathVariable String month) {
        checkAdmin((Claims) claims);
        ProjectUsageIdentity identity = new ProjectUsageIdentity();
        identity.setProjectDetailsId(id);
        identity.setMonthYear(month);
        return new ProjectDetailsInfo(projectService.deleteProjectUsage(id, identity));
    }

    // for Nodes Reservation/Booking in advance
    @PutMapping(path = "/usage/projects/{id}/nodesreservations")
    @ResponseStatus(HttpStatus.OK)
    public NodesReservation applyNodesReserve(@PathVariable Long id,
                                              @RequestBody @Valid NodesReservedInfo nodesResInfo,
                                              @AuthenticationPrincipal Object claims) {
        checkClaimsType(claims);
        checkAdmin((Claims) claims);
        return projectService.applyNodesReserve(id, nodesResInfo, ((Claims) claims).getSubject());
    }
}
