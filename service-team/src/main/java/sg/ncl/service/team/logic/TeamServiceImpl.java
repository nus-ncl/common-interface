package sg.ncl.service.team.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.*;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Inject
    protected TeamServiceImpl(final TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public Team addTeam(final Team team) {
        TeamEntity entity = new TeamEntity();

        entity.setName(team.getName());
        entity.setDescription(team.getDescription());
        entity.setWebsite(team.getWebsite());
        entity.setOrganisationType(team.getOrganisationType());
        entity.setApplicationDate(ZonedDateTime.now());

        return teamRepository.save(entity);
    }

    @Transactional
    public void removeTeam(final String id) {
        // check if entity exists first
        TeamEntity entity = findTeam(id);
        teamRepository.delete(id);
    }

    @Transactional
    public List<? extends Team> getTeams() {
        return teamRepository.findAll();
    }

    @Transactional
    public List<? extends Team> getTeamsByVisibility(final TeamVisibility visibility) {
        return teamRepository.findByVisibility(visibility);
    }

    @Transactional
    public Team getTeamById(final String id) {
        return findTeam(id);
    }

    @Transactional
    public Team getTeamByName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new TeamNameNullException();
        }
        return teamRepository.findByName(name);
    }

    @Transactional
    public Team updateTeam(final String id, final Team team) {

        // Note: team name should be unchangeable
        final TeamEntity entity = findTeam(id);

        if (team.getDescription() != null) {
            entity.setDescription(team.getDescription());
        }

        if (team.getPrivacy() != null) {
            entity.setPrivacy(team.getPrivacy());
        }

        if (team.getStatus() != null) {
            entity.setStatus(team.getStatus());
        }

        if (team.getVisibility() != null) {
            entity.setVisibility(team.getVisibility());
        }

        if (team.getWebsite() != null) {
            entity.setWebsite(team.getWebsite());
        }

        return teamRepository.save(entity);
    }

    @Transactional
    public Team addTeamMember(final String id, final TeamMember teamMember) {
        TeamEntity entity = findTeam(id);
        entity.addMember(teamMember);
        return teamRepository.save(entity);
    }

    @Transactional
    public boolean isTeamOwner(final String userId, final String teamId) {
        TeamEntity entity = findTeam(teamId);
        List<? extends TeamMember> teamMembersList = entity.getMembers();
        for (TeamMember teamMember: teamMembersList) {
            if (teamMember.getUserId().equals(userId) && teamMember.getMemberType().equals(TeamMemberType.OWNER)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public TeamMember changeTeamMemberStatus(String userId, String teamId, TeamMemberStatus teamMemberStatus) {
        TeamEntity entity = findTeam(teamId);
        TeamMember member = entity.getMember(userId);

        if (member == null) {
            throw new TeamMemberNotFoundException();
        }
        return entity.changeMemberStatus(member, teamMemberStatus);
    }

    @Transactional
    public Team changeTeamStatus(String teamId, TeamStatus teamStatus) {
        TeamEntity entity = findTeam(teamId);

        if (hasTeamOwner(teamId) == false) {
            throw new NoOwnerInTeamException();
        }

        entity.setStatus(teamStatus);
        entity.setProcessedDate(ZonedDateTime.now());
        return entity;
    }

    private TeamEntity findTeam(final String id) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullException();
        }

        final TeamEntity team = teamRepository.findOne(id);
        if (team == null) {
            throw new TeamNotFoundException(id);
        }
        return team;
    }

    private boolean hasTeamOwner(final String teamId) {
        TeamEntity entity = findTeam(teamId);
        List<? extends TeamMember> teamMembersList = entity.getMembers();
        for (TeamMember teamMember: teamMembersList) {
            if (teamMember.getMemberType().equals(TeamMemberType.OWNER)) {
                return true;
            }
        }
        return false;
    }
}
