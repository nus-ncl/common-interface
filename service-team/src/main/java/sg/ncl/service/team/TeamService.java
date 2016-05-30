package sg.ncl.service.team;

import org.springframework.stereotype.Service;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Inject
    protected TeamService(final TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team save(Team team) {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(team.getName());
        teamEntity.setDescription(team.getDescription());
        teamEntity.setApplicationDate(team.getApplicationDate());

        return teamRepository.save(teamEntity);
    }

    public List<Team> get() {
        final List<Team> result = new ArrayList<>();
        for (TeamEntity team : teamRepository.findAll()) {
            result.add(team);
        }
        return result;
    }

    public TeamEntity find(final String id) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullException();
        }

        final TeamEntity one = teamRepository.findOne(id);
        if (one == null) {
            throw new TeamNotFoundException();
        }
        return one;
    }
}
