package sg.ncl.service.team.domain;

import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
public interface TeamService {

    Team createTeam(Team team);

    List<? extends Team> getTeams();

    List<? extends Team> getTeamsByVisibility(final TeamVisibility visibility);

    Team getTeamById(final String id);

    Team getTeamByName(final String name);

    Team updateTeam(final String id, final Team team);

    Team addTeamMember(final String id, final TeamMember teamMember);
}
