package sg.ncl.service.team.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.logic.TeamService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsController {

    private final TeamService teamService;

    @Inject
    protected TeamsController(final TeamService teamService) {
        this.teamService = teamService;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createTeam(@RequestBody @Valid TeamInfo team) {
        teamService.createTeam(team);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Team> getAll() {
        List<Team> result = new ArrayList<>();
        for (Team team: teamService.getAll()) {
            result.add(new TeamInfo(team));
        }
        return result;
    }

    @RequestMapping(path = "/public", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Team> getPublicTeams() {
        List<Team> result = new ArrayList<>();
        for (Team team: teamService.getPublic()) {
            result.add(new TeamInfo(team));
        }
        return result;
    }

    @RequestMapping(path = "/name/{name}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Team getByName(@PathVariable String name) {
        return new TeamInfo(teamService.getName(name));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Team getTeam(@PathVariable String id) {
        return new TeamInfo(teamService.findTeam(id));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateTeam(@PathVariable String id, @RequestBody TeamInfo team) {
        teamService.updateTeam(id, team);
    }

    @RequestMapping(path = "/addUserToTeam/{id}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addUserToTeam(@PathVariable String id, @RequestBody TeamMemberInfo teamMember) {
        // id is the team id
        teamService.addUserToTeam(id, teamMember);
    }
}
