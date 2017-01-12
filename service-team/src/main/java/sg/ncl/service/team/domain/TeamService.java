package sg.ncl.service.team.domain;

import java.util.List;

/**
 * Created by Desmond/Te Ye
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

    Team addMember(String id, TeamMember teamMember);

    Team removeMember(String id, TeamMember teamMember);

    Team removeMember(String id, TeamMember teamMember, String requesterId);

    Boolean isOwner(String teamId, String userId);

    String findTeamOwner(String teamId);

    TeamMember updateMemberStatus(String teamId, String userId, MemberStatus status);

}
