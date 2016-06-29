package sg.ncl.service.team;

import org.springframework.stereotype.Service;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamNameNullException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import javax.inject.Inject;
import java.time.ZonedDateTime;
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

    public TeamEntity save(TeamEntity team) {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(team.getName());
        teamEntity.setDescription(team.getDescription());
        teamEntity.setWebsite(team.getWebsite());
        teamEntity.setOrganisationType(team.getOrganisationType());
        teamEntity.setApplicationDate(team.getApplicationDate());

        return teamRepository.save(teamEntity);
    }

    public List<TeamEntity> get() {
        final List<TeamEntity> result = new ArrayList<>();
        for (TeamEntity team : teamRepository.findAll()) {
            result.add(team);
        }
        return result;
    }

    public List<TeamEntity> getPublic() {
        List<TeamEntity> result = teamRepository.findByVisibility(TeamVisibility.PUBLIC);
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

    public TeamEntity getName(String name) {
        if (name == null || name.isEmpty()) {
            throw new TeamNameNullException();
        }

        final TeamEntity one = teamRepository.findByName(name);
        if (one == null) {
            throw new TeamNotFoundException();
        }
        return one;
    }

    public String getTeamStatus(final String id) {
        final TeamEntity one = this.find(id);
        return one.getStatus().toString();
    }

    public void update(final Team inputTeam) {
        final TeamEntity one = find(inputTeam.getId());

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

    public void addUserToTeam(final String teamId, TeamMemberInfo teamMemberInfo) {
            TeamEntity teamEntity = find(teamId);
            teamEntity.addMember(teamMemberInfo);
            teamRepository.save(teamEntity);
    }

    public void seedData() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName("Aries");
        teamEntity.setDescription("This is a project description");
        teamEntity.setApplicationDate(ZonedDateTime.now());
        teamEntity.setOrganisationType("Academic");
        teamEntity.setWebsite("http://www.nus.edu.sg");
        teamRepository.save(teamEntity);
    }
}
