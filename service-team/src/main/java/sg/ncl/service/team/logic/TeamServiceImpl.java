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
import sg.ncl.service.team.exceptions.*;

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
        if (team.getName() == null || team.getName().isEmpty()) {
            log.warn("Create team error: team name null or empty");
            throw new TeamNameNullOrEmptyException();
        }

        if(!isTeamNameValid(team.getName())) {
            log.warn("Create team error: invalid team name {}", team.getName());
            throw new InvalidTeamNameException("Team name " + team.getName() + " invalid");
        }

        if (teamRepository.findByName(team.getName()) != null) {
            log.warn("Create team error: team name {} already exists", team.getName());
            throw new TeamNameAlreadyExistsException("Team name " + team.getName() + " already exists");
        }

        log.info("Creating new team: {}", team.getName());

        TeamEntity entity = new TeamEntity();
        entity.setApplicationDate(ZonedDateTime.now());
        entity.setDescription(team.getDescription());
        entity.setName(team.getName());
        entity.setOrganisationType(team.getOrganisationType());
        entity.setWebsite(team.getWebsite());
        entity.setVisibility(team.getVisibility());
        entity.setStatus(TeamStatus.PENDING);

        final Team savedTeam = teamRepository.save(entity);
        log.info("New team created and saved: {}", savedTeam);

        return savedTeam;
    }

    @Transactional
    public void removeTeam(@NotNull final String id) {
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            log.warn("Remove team error: team {} not found", id);
            throw new TeamNotFoundException(id);
        }
        teamRepository.delete(id);
        log.info("Team {} removed", id);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream().collect(Collectors.toList());
    }

    public List<Team> getTeamsByVisibility(@NotNull final TeamVisibility visibility) {
        return teamRepository.findByVisibility(visibility).stream().collect(Collectors.toList());
    }

    public Team getTeamById(@NotNull final String id) {
        return findTeam(id);
    }

    public Team getTeamByName(@NotNull final String name) {
        return teamRepository.findByName(name);
    }

    // for user to update team profile
    @Transactional
    public Team updateTeam(@NotNull final String id, @NotNull final Team team) {
        // Note: team name should be unchangeable
        final TeamEntity entity = findTeam(id);
        if (entity == null) {
            log.warn("Update team profile error: team {} not found", id);
            throw new TeamNotFoundException(id);
        }

        log.info("Updating team profile: team to update {}, new profile {}", id, team);
        if (team.getDescription() != null) {
            entity.setDescription(team.getDescription());
        }
        if (team.getWebsite() != null) {
            entity.setWebsite(team.getWebsite());
        }
        if(team.getOrganisationType() != null) {
            entity.setOrganisationType(team.getOrganisationType());
        }
        if (team.getVisibility() != null) {
            entity.setVisibility(team.getVisibility());
        }
        if (team.getPrivacy() != null) {
            entity.setPrivacy(team.getPrivacy());
        }

        final Team updatedTeam = teamRepository.save(entity);
        log.info("Team profile updated: {}", updatedTeam);

        return updatedTeam;
    }

    @Transactional
    public Team addMember(@NotNull final String id, @NotNull final TeamMember teamMember) {
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            log.warn("Add team member error: team {} not found", id);
            throw new TeamNotFoundException(id);
        }
        log.info("Adding new member {} to team {}", teamMember.getUserId(), id);
        final TeamMember newMember = entity.addMember(teamMember);
        if(newMember == null) {
            log.warn("Add team member error: member {} already exists", teamMember.getUserId());
            throw new TeamMemberAlreadyExistsException("Member " + teamMember.getUserId() + " already exists");
        }
        final Team updatedTeam = teamRepository.save(entity);
        log.info("New member {} added to team {}", teamMember.getUserId(), updatedTeam.getId());
        return updatedTeam;
    }

    @Transactional
    public Team removeMember(@NotNull final String id, @NotNull final TeamMember teamMember) {
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            log.warn("Remove team member error: team {} not found", id);
            throw new TeamNotFoundException(id);
        }
        log.info("Removing member {} from team {}", teamMember.getUserId(), id);
        entity.changeMemberStatus(teamMember, MemberStatus.REJECTED);
        final Team updatedTeam = teamRepository.save(entity);
        log.info("Member {} removed from team {}", teamMember.getUserId(), updatedTeam.getId());
        return updatedTeam;
    }

    public Boolean isOwner(@NotNull final String teamId, @NotNull final String userId) {
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            log.warn("Check is team owner error: team {} not found", teamId);
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
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            log.warn("Update member status error: team {} not found", teamId);
            throw new TeamNotFoundException(teamId);
        }

        TeamMember member = entity.getMember(userId);
        if (member == null) {
            log.warn("Update member status error: user {} is not a member of team {}", userId, teamId);
            throw new TeamMemberNotFoundException("User " + userId + " is not a member of team " + teamId);
        }

        log.info("Update member status: user {}, new status {}", userId, status);
        return entity.changeMemberStatus(member, status);
    }

    @Transactional
    public Team updateTeamStatus(@NotNull final String id, @NotNull final TeamStatus status) {
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            log.warn("Update team status error: team {} not found", id);
            throw new TeamNotFoundException(id);
        }

        log.info("Updating team status: team {}, new status {}", id, status);

        switch (status) {
            case APPROVED:
                if (entity.getStatus().equals(status.PENDING) || entity.getStatus().equals(status.RESTRICTED)) {
                    return updateTeamStatusInternal(entity, status.APPROVED);
                } else {
                    throw new InvalidStatusTransitionException(entity.getStatus() + " -> " + status);
                }
            case RESTRICTED:
                if (entity.getStatus().equals(TeamStatus.APPROVED)) {
                    return updateTeamStatusInternal(entity, status.RESTRICTED);
                } else {
                    throw new InvalidStatusTransitionException(entity.getStatus() + " -> " + status);
                }
            case CLOSED:
                return updateTeamStatusInternal(entity, status.CLOSED);
            default:
                log.warn("Update team status failed for {}: unknown status {}", id, status);
                throw new InvalidTeamStatusException(status.toString());
        }
    }

    private Team updateTeamStatusInternal(TeamEntity entity, TeamStatus status) {
        TeamStatus oldStatus = entity.getStatus();
        entity.setStatus(status);

        if (status.equals(TeamStatus.APPROVED)) {
            // after approving new team application
            // set the processed date
            entity.setProcessedDate(ZonedDateTime.now());
        } else {
            // set the last modified date for other changes
            entity.setLastModifiedDate(ZonedDateTime.now());
        }

        TeamEntity savedTeam = teamRepository.save(entity);
        log.info("Status updated for team {}: {} -> {}", entity.getId(), oldStatus, entity.getStatus());
        return savedTeam;
    }

    private TeamEntity findTeam(final String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return teamRepository.findOne(id);
    }

    private boolean hasTeamOwner(@NotNull final String teamId) {
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            log.warn("Check has team owner error: team {} not found", teamId);
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

    private boolean isTeamNameValid(@NotNull final String name) {
        return  name.trim().length() >= 2 &&
                name.trim().length() <= 12 &&
                name.matches("^([-\\w]+)$") &&
                !name.trim().startsWith("-");
    }
}
