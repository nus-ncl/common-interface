package sg.ncl.service.team.domain;

import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
public interface TeamService {

    Team addTeam(Team team);

    void removeTeam(String id);

    List<? extends Team> getTeams();

    List<? extends Team> getTeamsByVisibility(TeamVisibility visibility);

    Team getTeamById(String id);

    Team getTeamByName(String name);

    Team updateTeam(String id, Team team);

    Team addTeamMember(String id, TeamMember teamMember);

    Team removeTeamMember(String id, TeamMember teamMember);

    boolean isTeamOwner(final String userId, final String teamId);

    TeamMember changeTeamMemberStatus(String userId, String teamId, TeamMemberStatus approved);

    Team changeTeamStatus(String teamId, TeamStatus approved);
}
