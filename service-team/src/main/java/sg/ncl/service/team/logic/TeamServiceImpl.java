package sg.ncl.service.team.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamMemberEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.exceptions.NoOwnerInTeamException;
import sg.ncl.service.team.exceptions.TeamIdNullOrEmptyException;
import sg.ncl.service.team.exceptions.TeamMemberNotFoundException;
import sg.ncl.service.team.exceptions.TeamNameNullOrEmptyException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.exceptions.UserIdNullOrEmptyException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Desmond/Te Ye
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Inject
    TeamServiceImpl(final TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public Team createTeam(@NotNull final Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team object is NULL");
        }
        if (team.getName() == null || team.getName().isEmpty()) {
            throw new TeamNameNullOrEmptyException();
        }
        // FIXME: need to check whether team already exists or not
        if (teamRepository.findByName(team.getName()) != null) {
            // throw new TeamAlreadyExistsException();
        }

        TeamEntity entity = new TeamEntity();
        entity.setName(team.getName());
        entity.setDescription(team.getDescription());
        entity.setWebsite(team.getWebsite());
        entity.setOrganisationType(team.getOrganisationType());
        entity.setApplicationDate(ZonedDateTime.now());

        return teamRepository.save(entity);
    }

    @Transactional
    public void removeTeam(@NotNull final String id) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            throw new TeamNotFoundException(id);
        }
        teamRepository.delete(id);
    }

    @Transactional
    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream().collect(Collectors.toList());
    }

    @Transactional
    public List<Team> getTeamsByVisibility(@NotNull final TeamVisibility visibility) {
        return teamRepository.findByVisibility(visibility).stream().collect(Collectors.toList());
    }

    @Transactional
    public Team getTeamById(@NotNull final String id) {
        return findTeam(id);
    }

    @Transactional
    public Team getTeamByName(@NotNull final String name) {
        if (name == null || name.isEmpty()) {
            throw new TeamNameNullOrEmptyException();
        }
        return teamRepository.findByName(name);
    }

    @Transactional
    public Team updateTeam(@NotNull final String id, @NotNull final Team team) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        if (team == null) {
            throw new IllegalArgumentException("Team object is NULL");
        }
        // Note: team name should be unchangeable
        final TeamEntity entity = findTeam(id);
        if (entity == null) {
            throw new TeamNotFoundException(id);
        }

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
    public Team addMember(@NotNull final String id, @NotNull final TeamMember teamMember) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        if (teamMember == null) {
            throw new IllegalArgumentException("TeamMember object null");
        }
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            throw new TeamNotFoundException(id);
        }
        entity.addMember(teamMember);
        return teamRepository.save(entity);
    }

    @Transactional
    public Team removeMember(@NotNull final String id, @NotNull final TeamMember teamMember) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        if (teamMember == null) {
            throw new IllegalArgumentException("TeamMember object null");
        }
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            throw new TeamNotFoundException(id);
        }
        entity.removeMember(teamMember);
        return teamRepository.save(entity);
    }

    @Transactional
    public Boolean isOwner(@NotNull final String teamId, @NotNull final String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new UserIdNullOrEmptyException();
        }
        if (teamId == null || teamId.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            throw new TeamNotFoundException(teamId);
        }
        List<TeamMemberEntity> teamMembersList = entity.getMembers();
        for (TeamMember teamMember : teamMembersList) {
            if (teamMember.getUserId().equals(userId) && teamMember.getMemberType().equals(MemberType.OWNER)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public TeamMember updateMemberStatus(@NotNull final String teamId, @NotNull final String userId, @NotNull final MemberStatus status) {
        if (userId == null || userId.isEmpty()) {
            throw new UserIdNullOrEmptyException();
        }
        if (teamId == null || teamId.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        if (status == null) {
            throw new IllegalArgumentException("MemberStatus object null");
        }
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            throw new TeamNotFoundException(teamId);
        }

        TeamMember member = entity.getMember(userId);
        if (member == null) {
            throw new TeamMemberNotFoundException();
        }

        return entity.changeMemberStatus(member, status);
    }

    @Transactional
    public Team updateTeamStatus(String id, TeamStatus teamStatus) {
        if (id == null || id.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }
        if (teamStatus == null) {
            throw new IllegalArgumentException("TeamStatus object null");
        }

        TeamEntity entity = findTeam(id);
        if (entity == null) {
            throw new TeamNotFoundException(id);
        }

        // FIXME: why need to check team owner exists here???
        if (hasTeamOwner(id) == false) {
            throw new NoOwnerInTeamException();
        }

        entity.setStatus(teamStatus);
        entity.setProcessedDate(ZonedDateTime.now());

        // FIXME: why don't save(entity)???
        return entity;
    }

    private TeamEntity findTeam(final String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        return teamRepository.findOne(id);
    }

    private boolean hasTeamOwner(final String teamId) {
        if (teamId == null || teamId.isEmpty()) {
            throw new TeamIdNullOrEmptyException();
        }

        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            throw new TeamNotFoundException(teamId);
        }

        List<TeamMemberEntity> teamMembersList = entity.getMembers();
        for (TeamMember teamMember : teamMembersList) {
            if (teamMember.getMemberType().equals(MemberType.OWNER)) {
                return true;
            }
        }
        return false;
    }
}
