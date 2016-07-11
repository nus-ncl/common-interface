package sg.ncl.service.team.domain;

import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
public interface TeamService {

    Team addTeam(Team team);

    List<? extends Team> getTeams();

    List<? extends Team> getTeamsByVisibility(TeamVisibility visibility);

    Team getTeamById(String id);

    Team getTeamByName(String name);

    Team updateTeam(String id, Team team);

    Team addTeamMember(String id, TeamMember teamMember);
}
