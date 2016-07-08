package sg.ncl.service.team.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsController {

    private static final Logger logger = LoggerFactory.getLogger(TeamsController.class);

    private final TeamService teamService;

    @Inject
    protected TeamsController(final TeamService teamService) {
        this.teamService = teamService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@RequestBody @Valid final TeamInfo team) {
        logger.info("Creating a team: {}", team);
        return new TeamInfo(teamService.createTeam(team));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Team> getTeams() {
        return teamService.getTeams().stream().map(TeamInfo::new).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, params = {"visibility"})
    @ResponseStatus(HttpStatus.OK)
    public List<Team> getTeamsByVisibility(@RequestParam("visibility") final TeamVisibility visibility) {
        return teamService.getTeamsByVisibility(visibility).stream().map(TeamInfo::new).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public Team getTeamByName(@RequestParam("name") final String name) {
        final Team team = teamService.getTeamByName(name);
        if (team == null) {
            throw new TeamNotFoundException(name);
        }
        return new TeamInfo(team);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Team getTeamById(@PathVariable final String id) {
        final Team team = teamService.getTeamById(id);
        if (team == null) {
            throw new TeamNotFoundException(id);
        }
        return new TeamInfo(team);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Team updateTeam(@PathVariable final String id, @RequestBody final TeamInfo team) {
        return new TeamInfo(teamService.updateTeam(id, team));
    }

    @RequestMapping(path = "/{id}/members", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Team addTeamMember(@PathVariable final String id, @RequestBody final TeamMemberInfo teamMember) {
        // id is the team id
        return new TeamInfo(teamService.addTeamMember(id, teamMember));
    }
}
