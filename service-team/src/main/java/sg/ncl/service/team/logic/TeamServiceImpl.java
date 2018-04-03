package sg.ncl.service.team.logic;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.AccountingProperties;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.team.data.jpa.*;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.*;
import sg.ncl.service.user.domain.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Desmond/Te Ye
 */
@Service
@Slf4j
@EnableConfigurationProperties(AccountingProperties.class)
public class TeamServiceImpl implements TeamService {

    private static final String NOT_ALLOWED = "is not allowed";

    private final AdapterDeterLab adapterDeterLab;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final TeamQuotaRepository teamQuotaRepository;
    private final AnalyticsService analyticsService;
    private final AccountingProperties accountingProperties;

    @Inject
    TeamServiceImpl(final AdapterDeterLab adapterDeterLab, final TeamRepository teamRepository, final UserService userService, final TeamQuotaRepository teamQuotaRepository, @NotNull final AnalyticsService analyticsService, @NotNull final AccountingProperties accountingProperties) {
        this.adapterDeterLab = adapterDeterLab;
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.teamQuotaRepository = teamQuotaRepository;
        this.analyticsService = analyticsService;
        this.accountingProperties = accountingProperties;
    }

    @Override
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

    @Override
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

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public List<Team> getTeamsByVisibility(@NotNull final TeamVisibility visibility) {
        return teamRepository.findByVisibility(visibility).stream().collect(Collectors.toList());
    }

    @Override
    public Team getTeamById(@NotNull final String id) {
        return findTeam(id);
    }

    @Override
    public Team getTeamByName(@NotNull final String name) {
        return teamRepository.findByName(name);
    }

    // for user to update team profile
    @Override
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

    @Override
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
    @Override
    @Transactional
    public Team removeMember(@NotNull final String id, @NotNull final TeamMember teamMember) {
        TeamEntity entity = findTeam(id);
        if (entity == null) {
            log.warn("Remove team member error: team {} not found", id);
            throw new TeamNotFoundException(id);
        }
        log.info("Removing member {} from team {}", teamMember.getUserId(), id);
        entity.removeMember(teamMember);
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
    @Override
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

    @Override
    public Boolean isMember(@NotNull final String teamId, @NotNull final String userId) {
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            log.warn("Team {} not found", teamId);
            throw new TeamNotFoundException(teamId);
        }
        List<TeamMemberEntity> teamMembersList = entity.getMembers();
        for (TeamMember teamMember : teamMembersList) {
            if (teamMember.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
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

    @Override
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

    @Override
    public String findTeamOwner(@NotNull final String teamId) {
        TeamEntity entity = findTeam(teamId);
        if (entity == null) {
            log.warn("Check has team owner error: team {} not found", teamId);
            throw new TeamNotFoundException(teamId);
        }

        List<TeamMemberEntity> teamMembersList = entity.getMembers();
        for (TeamMember teamMember : teamMembersList) {
            if (teamMember.getMemberType().equals(MemberType.OWNER)) {
                return teamMember.getUserId();
            }
        }
        return null;
    }

    private boolean isTeamNameValid(@NotNull final String name) {
        return  name.trim().length() >= 2 &&
                name.trim().length() <= 12 &&
                name.matches("^([-\\w]+)$") &&
                !name.trim().startsWith("-");
    }

    @Override
    public TeamQuota getTeamQuotaByTeamId(@NotNull String teamId) {

        if (teamId == null || teamId.isEmpty()) {
            log.warn("Update team quota error: team id is null or empty");
            return null;
        }

        //check if team exists
        Team team = teamRepository.findOne(teamId);
        if (team == null) {
            log.warn("Get team quota error: team {} not found", teamId);
            throw new TeamNotFoundException(teamId);
        }

        TeamQuotaEntity teamQuotaEntity = teamQuotaRepository.findByTeamId(teamId);
        if (teamQuotaEntity == null) {
            teamQuotaEntity = new TeamQuotaEntity();
            teamQuotaEntity.setTeamId(teamId);
            teamQuotaEntity.setQuota(null);
            teamQuotaEntity.setId(null);
            return teamQuotaEntity;
        }

        return teamQuotaEntity;
    }

    @Override
    @Transactional //allow database to roll back in case of error
    public TeamQuota updateTeamQuota(@NotNull String teamId, @NotNull TeamQuota teamQuota){
        if (teamId == null || teamId.isEmpty()) {
            log.warn("Update team quota error: team id is null or empty");
            return null;
        }

        //check if team exists
        Team team = teamRepository.findOne(teamId);
        if (team == null) {
            log.warn("Update team quota error: team {} not found", teamId);
            throw new TeamNotFoundException(teamId);
        }

        //check if budget is negative or exceed limit
        if (teamQuota.getQuota() != null) {
            if (teamQuota.getQuota().compareTo(BigDecimal.valueOf(0)) < 0 ||
                    teamQuota.getQuota().compareTo(BigDecimal.valueOf(99999999.99)) > 0) {
                throw new TeamQuotaOutOfRangeException(teamId);
            }
        }

        TeamQuotaEntity teamQuotaEntity = teamQuotaRepository.findByTeamId(teamId);
        if (teamQuotaEntity == null) {
            teamQuotaEntity = new TeamQuotaEntity();
            teamQuotaEntity.setTeamId(teamId);
        }

        log.info("Updating team quota: team to be update {}, team quota to be updated {}", teamId, teamQuotaEntity);
        teamQuotaEntity.setQuota(teamQuota.getQuota());
        final TeamQuota updatedTeamQuota = teamQuotaRepository.save(teamQuotaEntity);
        log.info("Team quota has been updated: {}",  updatedTeamQuota);

        return updatedTeamQuota;
    }

    @Override
    public int checkTeamQuota(String teamName) {

        //getting team quota
        Team team = getTeamByName(teamName);
        String teamId = team.getId();
        TeamQuota teamQuota = getTeamQuotaByTeamId(teamId);
        BigDecimal quota = teamQuota.getQuota();
        BigDecimal charges = new BigDecimal(accountingProperties.getCharges());

        //getting usage , if quota is null => unlimited quota
        if (quota != null) {
            ZonedDateTime startDate = team.getApplicationDate();
            ZonedDateTime endDate = ZonedDateTime.now();
            String usageinString = analyticsService.getUsageStatistics(teamId, startDate, endDate);
            BigDecimal usageInBD = new BigDecimal(usageinString);
            usageInBD = usageInBD.multiply(charges);

            //comparing quota and usage
            if (quota.compareTo(usageInBD) <= 0) {
                return -1;
            }
        }

        return 1;
    }

    @Override
    public String getReservationStatus(@NotNull String teamId) {
        return adapterDeterLab.getReservationStatus(teamId);
    }

}
