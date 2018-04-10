package sg.ncl.service.team.domain;

import java.util.List;

/**
 * @Author Desmond, Te Ye, Tran Ly Vu
 */
public interface TeamService {

    Team createTeam(Team team);

    void removeTeam(String id);

    List<Team> getAllTeams();

    List<Team> getTeamsByVisibility(TeamVisibility visibility);

    Team getTeamById(String id);

    Team getTeamByName(String name);

    Team updateTeam(String id, Team team);

    Team updateTeamStatus(String id, TeamStatus status);

    TeamQuota updateTeamQuota(String teamId, TeamQuota teamQuota);

    Team addMember(String id, TeamMember teamMember);

    Team removeMember(String id, TeamMember teamMember);

    Team removeMember(String id, TeamMember teamMember, String requesterId);

    Boolean isOwner(String teamId, String userId);

    String findTeamOwner(String teamId);

    TeamMember updateMemberStatus(String teamId, String userId, MemberStatus status);

    TeamQuota getTeamQuotaByTeamId(String teamId);

    int checkTeamQuota(String teamName);

    Boolean isMember(String teamId, String userId);

    String getReservationStatus(String teamId);

    String releaseNodes(String teamId, String numNodes);
}
