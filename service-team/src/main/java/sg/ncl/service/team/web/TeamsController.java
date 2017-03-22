package sg.ncl.service.team.web;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.team.data.jpa.TeamQuotaEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.exceptions.TeamQuotaOutOfRangeException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.common.validation.Validator.checkClaimsType;
import static sg.ncl.service.team.validations.Validator.isAdmin;
import static sg.ncl.service.team.web.TeamsController.PATH;

/**
 * @author Christopher Zhong, Tran Ly Vu
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TeamsController {

    static final String PATH = "/teams";

    private final TeamService teamService;
    private final AnalyticsService analyticsService;

    @Inject
    TeamsController(final TeamService teamService,
                    final AnalyticsService analyticsService) {
        this.teamService = teamService;
        this.analyticsService = analyticsService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Team> getTeams() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // throw forbidden
            log.warn("Access denied for: /teams GET");
            throw new ForbiddenException();
        }
        return teamService.getAllTeams().stream().map(TeamInfo::new).collect(Collectors.toList());
    }

    @GetMapping(params = {"visibility"})
    @ResponseStatus(HttpStatus.OK)
    public List<Team> getTeamsByVisibility(@RequestParam("visibility") final TeamVisibility visibility) {
        if ((visibility != TeamVisibility.PUBLIC) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
            // check permissions
            // throw forbidden if check fails
            log.warn("Access denied for: /teams/?visibility=" + visibility);
            throw new ForbiddenException();
        }
        return teamService.getTeamsByVisibility(visibility).stream().map(TeamInfo::new).collect(Collectors.toList());
    }

    @GetMapping(params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public Team getTeamByName(@RequestParam("name") final String name) {
        final Team team = teamService.getTeamByName(name);
        if (team == null) {
            throw new TeamNotFoundException(name);
        }
        return new TeamInfo(team);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team getTeamById(@PathVariable final String id) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // throw forbidden
            log.warn("Access denied for: /teams/{} GET", id);
            throw new ForbiddenException();
        }
        final Team team = teamService.getTeamById(id);
        if (team == null) {
            throw new TeamNotFoundException(id);
        }
        return new TeamInfo(team);
    }

    @GetMapping(path = "/{teamId}/quota")
    @ResponseStatus(HttpStatus.OK)
    public TeamQuota getTeamQuotaByTeamId(@PathVariable final String teamId) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // throw forbidden
            log.warn("Access denied for: /teams/{}/quota GET", teamId);
            throw new ForbiddenException();
        }

        //check if team exists
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            throw new TeamNotFoundException(teamId);
        }

        // applicationDate and processedDate are not different here
        ZonedDateTime startDate = team.getApplicationDate();
        ZonedDateTime endDate = ZonedDateTime.now();

        String usage = analyticsService.getUsageStatistics(teamId, startDate, endDate);
        TeamQuota teamQuota = teamService.getTeamQuotaByTeamId(teamId);

        if (teamQuota == null) {
            TeamQuotaEntity teamQuotaEntity = new TeamQuotaEntity();
            teamQuotaEntity.setTeamId(teamId);
            teamQuotaEntity.setQuota(null);
            teamQuotaEntity.setId(null);
            return new TeamQuotaInfo(teamQuotaEntity, usage);
        }

        return new TeamQuotaInfo(teamQuota, usage);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team updateTeam(@PathVariable final String id, @RequestBody final TeamInfo team) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.warn("Access denied for: /teams/{} PUT", id);
            throw new ForbiddenException();
        }
        return new TeamInfo(teamService.updateTeam(id, team));
    }

    @PutMapping(path = "/{teamId}/quota")
    @ResponseStatus(HttpStatus.OK)
    public TeamQuota updateTeamQuota(@PathVariable final String teamId, @RequestBody final TeamQuotaInfo teamQuotaInfo){
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.warn("Access denied for: /teams/{} PUT", teamId);
            throw new ForbiddenException();
        }

        //check if budget is negative or exceed limit
        if (teamQuotaInfo.getQuota().compareTo(BigDecimal.valueOf(0)) < 0 ||
                teamQuotaInfo.getQuota().compareTo(BigDecimal.valueOf(99999999.99)) > 0) {
            throw new TeamQuotaOutOfRangeException(teamId);
        }

        //check if team exists
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            throw new TeamNotFoundException(teamId);
        }

        ZonedDateTime startDate = team.getApplicationDate();
        ZonedDateTime endDate = ZonedDateTime.now();
        String usage = analyticsService.getUsageStatistics(teamId, startDate, endDate);

        return new TeamQuotaInfo(teamService.updateTeamQuota(teamId, teamQuotaInfo), usage);
    }

    // for admin to update team status
    @PutMapping(path = "/{id}/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public Team updateTeamStatus(@AuthenticationPrincipal final Object claims, @PathVariable final String id, @PathVariable final TeamStatus status) {
        if (claims == null || !(claims instanceof Claims)) {
            throw new UnauthorizedException();
        }
        isAdmin((Claims) claims);
        // check permissions
        return new TeamInfo(teamService.updateTeamStatus(id, status));
    }

    @PostMapping(path = "/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Team addTeamMember(@PathVariable final String id, @RequestBody final TeamMemberInfo teamMember) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.warn("Access denied for: /teams/{}/members POST", id);
            throw new ForbiddenException();
        }
        return new TeamInfo(teamService.addMember(id, teamMember));
    }

    // for team profile remove users from team
    @DeleteMapping(path = "/{id}/members")
    @ResponseStatus(HttpStatus.OK)
    public Team removeTeamMember(@PathVariable final String id, @RequestBody final TeamMemberInfo teamMember, @AuthenticationPrincipal final Object claims) {
        checkClaimsType(claims);
        return new TeamInfo(teamService.removeMember(id, teamMember, ((Claims) claims).getSubject()));
    }
}
