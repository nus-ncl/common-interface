package sg.ncl.service.team;

import org.springframework.stereotype.Service;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.dtos.TeamInfo;
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

    public TeamEntity save(Team team) {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(team.getName());
        teamEntity.setDescription(team.getDescription());
        teamEntity.setApplicationDate(team.getApplicationDate());

        return teamRepository.save(teamEntity);
    }

    public List<TeamInfo> get() {
        final List<TeamInfo> result = new ArrayList<>();
        for (TeamEntity team : teamRepository.findAll()) {
            TeamInfo teamInfo = new TeamInfo(team);
            result.add(teamInfo);
        }
        return result;
    }

    protected TeamEntity findAndReturnTeamEntity(final String id) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullException();
        }

        final TeamEntity one = teamRepository.findOne(id);
        if (one == null) {
            throw new TeamNotFoundException();
        }

        return one;
    }

    public TeamInfo find(final String id) {
        return new TeamInfo(findAndReturnTeamEntity(id));
    }

    public String getTeamStatus(final String id) {
        final TeamInfo one = this.find(id);
        return one.getStatus().toString();
    }

    public void update(final String teamId, final Team inputTeam) {
        final TeamEntity one = findAndReturnTeamEntity(inputTeam.getId());

        if (inputTeam.getDescription() != null) {
            one.setDescription(inputTeam.getDescription());
        }

        if (inputTeam.getPrivacy() != null) {
            one.setPrivacy(inputTeam.getPrivacy());
        }

        if (inputTeam.getStatus() != null) {
            one.setStatus(inputTeam.getStatus());
        }

        if (inputTeam.getVisibility() != null) {
            one.setVisibility(inputTeam.getVisibility());
        }

        teamRepository.save(one);
    }

    public boolean addUserToTeam(final String userId, final String teamId) {
        boolean noErrors = true;

        try {
            TeamEntity teamEntity = findAndReturnTeamEntity(teamId);
            teamEntity.addMember(userId);
            teamRepository.save(teamEntity);
        }

        catch (Exception e) {
            noErrors = false;
        }

        return noErrors;
    }
}
