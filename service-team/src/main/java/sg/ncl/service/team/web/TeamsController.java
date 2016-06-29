package sg.ncl.service.team.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.ncl.service.team.TeamService;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.dtos.TeamInfo;
import sg.ncl.service.team.dtos.TeamMemberInfo;

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
    public void addTeam(@RequestBody @Valid TeamEntity team) {
        teamService.save(team);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<TeamInfo> get() {
        List<TeamEntity> teamEntityList = teamService.get();
        List<TeamInfo> teamInfoList = new ArrayList<>();

        for (TeamEntity teamEntity : teamEntityList) {
            teamInfoList.add(new TeamInfo(teamEntity));
        }

        return teamInfoList;
    }

    @RequestMapping(path = "/public", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<TeamInfo> getPublicTeams() {
        List<TeamEntity> teamEntityList = teamService.getPublic();
        List<TeamInfo> teamInfoList = new ArrayList<>();

        for (TeamEntity teamEntity : teamEntityList) {
            teamInfoList.add(new TeamInfo(teamEntity));
        }
        return teamInfoList;
    }

    @RequestMapping(path = "/name/{name}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public TeamInfo getByName(@PathVariable String name) {
        return new TeamInfo(teamService.getName(name));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public TeamInfo getTeam(@PathVariable String id) {
        return new TeamInfo(teamService.find(id));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateTeam(@PathVariable String id, @RequestBody TeamEntity teamEntity) {
        teamService.update(teamEntity);
    }

    @RequestMapping(path = "/addUserToTeam/{id}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addUserToTeam(@PathVariable String id, @RequestBody TeamMemberInfo teamMember) {
        // id is the team id
        teamService.addUserToTeam(id, teamMember);
    }

    @RequestMapping(path = "/seed", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void seedData() {
        teamService.seedData();
    }
}
