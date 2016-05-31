package sg.ncl.service.team.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.domain.Team;

import javax.inject.Inject;
import javax.validation.Valid;
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
    public void addTeam(@RequestBody @Valid TeamEntity team) {
        teamService.save(team);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<Team> get() {
        return teamService.get();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Team getTeam(@PathVariable String id) {
        final TeamEntity teamEntity = teamService.find(id);
        return teamEntity;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateTeam(@PathVariable String id, @RequestBody TeamEntity teamEntity) {
        teamService.update(id, teamEntity);
    }
}
