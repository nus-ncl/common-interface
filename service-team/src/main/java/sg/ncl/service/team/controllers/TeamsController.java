package sg.ncl.service.team.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author Christopher Zhong
 */
@RestController
@RequestMapping(path = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsController {

    private final TeamRepository teamRepository;

    @Inject
    protected TeamsController(final TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addUser(@RequestBody @Valid Team user) {
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Team getUser(@PathVariable String id) {
        final TeamEntity team = teamRepository.findOne(id);
        return team;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void updateTeam(@PathVariable String id) {
    }

}
