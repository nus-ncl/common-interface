package sg.ncl.service.team.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamNameNullException;
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

    @Transactional
    public Team createTeam(Team team) {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(team.getName());
        teamEntity.setDescription(team.getDescription());
        teamEntity.setWebsite(team.getWebsite());
        teamEntity.setOrganisationType(team.getOrganisationType());
        teamEntity.setApplicationDate(team.getApplicationDate());

        return teamRepository.save(teamEntity);
    }

    @Transactional
    public List<Team> getAll() {
        final List<Team> result = new ArrayList<>();
        for (TeamEntity teamEntity : teamRepository.findAll()) {
            result.add(teamEntity);
        }
        return result;
    }

    @Transactional
    public List<Team> getByVisibility(TeamVisibility visibility) {
        List<TeamEntity> result = teamRepository.findByVisibility(visibility);
        List<Team> teamList = new ArrayList<>();

        for (TeamEntity teamEntity : result) {
            teamList.add(teamEntity);
        }
        return teamList;
    }

    @Transactional
    public Team getById(final String id) {
        return findTeamEntity(id);
    }

    @Transactional
    public Team getName(String name) {
        if (name == null || name.isEmpty()) {
            throw new TeamNameNullException();
        }

        final TeamEntity one = teamRepository.findByName(name);
        if (one == null) {
            throw new TeamNotFoundException();
        }
        return one;
    }

    @Transactional
    public String getTeamStatus(final String id) {
        final Team one = getById(id);
        return one.getStatus().toString();
    }

    @Transactional
    public void updateTeam(final String id, final Team team) {

        // Note: team name should be unchangeable
        final TeamEntity one = findTeamEntity(id);

        if (team.getDescription() != null) {
            one.setDescription(team.getDescription());
        }

        if (team.getPrivacy() != null) {
            one.setPrivacy(team.getPrivacy());
        }

        if (team.getStatus() != null) {
            one.setStatus(team.getStatus());
        }

        if (team.getVisibility() != null) {
            one.setVisibility(team.getVisibility());
        }

        teamRepository.save(one);
    }

    @Transactional
    public void addUserToTeam(final String teamId, TeamMember teamMemberInfo) {
            TeamEntity teamEntity = findTeamEntity(teamId);
            teamEntity.addMember(teamMemberInfo);
            teamRepository.save(teamEntity);
    }

    private TeamEntity findTeamEntity(final String id) {
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
