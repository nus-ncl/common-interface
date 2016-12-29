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
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static sg.ncl.common.validation.Validator.checkClaimsType;
import static sg.ncl.service.team.validations.Validator.isAdmin;
import static sg.ncl.service.team.web.TeamsController.PATH;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TeamsController {

    static final String PATH = "/teams";

    private final TeamService teamService;

    @Inject
    TeamsController(final TeamService teamService) {
        this.teamService = teamService;
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

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team updateTeam(@PathVariable final String id, @RequestBody final TeamInfo team) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.warn("Access denied for: /teams/{} PUT", id);
            throw new ForbiddenException();
        }
        return new TeamInfo(teamService.updateTeam(id, team));
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
        // FIXME check if requester id is the team owner
        return new TeamInfo(teamService.removeMember(id, teamMember, ((Claims) claims).getSubject()));
    }
}
