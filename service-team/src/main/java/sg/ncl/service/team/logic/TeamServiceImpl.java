package sg.ncl.service.team.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
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
import sg.ncl.service.user.domain.UserService;

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

    private static final String NOT_ALLOWED = "is not allowed";

    private final AdapterDeterLab adapterDeterLab;
    private final TeamRepository teamRepository;
    private final UserService userService;

    @Inject
    TeamServiceImpl(final AdapterDeterLab adapterDeterLab, final TeamRepository teamRepository, final UserService userService) {
        this.adapterDeterLab = adapterDeterLab;
        this.teamRepository = teamRepository;
        this.userService = userService;
    }

    @Transactional
    public Team createTeam(@NotNull final Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            log.warn("Create team error: team name null or empty");
            throw new TeamNameNullOrEmptyException();
        }

        if(!isTeamNameValid(team.getName())) {
            log.warn("Create team error: invalid team name {}", team.getName());
            throw new InvalidTeamNameException("Team name " + team.getName() + " is invalid");
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
            throw new TeamMemberAlreadyExistsException("Member already exists");
        }
        final Team updatedTeam = teamRepository.save(entity);
        log.info("New member {} added to team {}", teamMember.getUserId(), updatedTeam.getId());
        return updatedTeam;
    }

    /**
     * Remove users from team by updating the team member status; only affects the service-team
     * @param id The team id to remove users from
     * @param teamMember The team member details to be remove
     * @return The updated team with the specific user removed
     */
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

    /**
     * Remove users from team by also invoking service-user; for the web-service team profile page
     * @param id The team id to remove users from
     * @param teamMember The team member details to be removed
     * @param requesterId The user id of the one requesting to remove the particular user; this must be the team owner
     * @return The updated team with the specific user removed
     */
    @Transactional
    public Team removeMember(@NotNull final String id, @NotNull final TeamMember teamMember, @NotNull final String requesterId) {
        log.info("Removing member {} from team {} requested by {}", teamMember.getUserId(), id, requesterId);
        userService.removeTeam(teamMember.getUserId(), id);
        Team team = removeMember(id, teamMember);
        adapterDeterLab.removeUserFromTeam(id, teamMember.getUserId(), requesterId);
        return team;
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
                if (entity.getStatus().equals(TeamStatus.PENDING) || entity.getStatus().equals(TeamStatus.RESTRICTED)) {
                    return updateTeamStatusInternal(entity, TeamStatus.APPROVED);
                } else {
                    throw new InvalidStatusTransitionException(entity.getStatus() + " -> " + status + " " + NOT_ALLOWED);
                }
            case RESTRICTED:
                if (entity.getStatus().equals(TeamStatus.APPROVED)) {
                    return updateTeamStatusInternal(entity, TeamStatus.RESTRICTED);
                } else {
                    throw new InvalidStatusTransitionException(entity.getStatus() + " -> " + status + " " + NOT_ALLOWED);
                }
            case REJECTED:
            case CLOSED:
                return updateTeamStatusInternal(entity, TeamStatus.CLOSED);
            default:
                log.warn("Update team status failed for {}: unknown status {}", id, status);
                throw new InvalidTeamStatusException("Invalid team status " + status);
        }
    }

    private Team updateTeamStatusInternal(TeamEntity entity, TeamStatus status) {
        final TeamStatus oldStatus = entity.getStatus();
        final ZonedDateTime now = ZonedDateTime.now();

        entity.setStatus(status);
        entity.setLastModifiedDate(now);

        if(oldStatus.equals(TeamStatus.PENDING) && status.equals(TeamStatus.APPROVED)) {
            entity.setProcessedDate(now);
        }

        final TeamEntity savedTeam = teamRepository.save(entity);
        log.info("Status updated for team {}: {} -> {}", entity.getId(), oldStatus, savedTeam.getStatus());
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
