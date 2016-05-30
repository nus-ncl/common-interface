package sg.ncl.service.team;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
public class TeamServiceTest extends AbstractTest {
    @Inject
    private TeamRepository teamRepository;

    @Test
    public void saveTeamTest() {
        TeamService teamService = new TeamService(teamRepository);
        Team createdTeam = createTeam();
        Team saveTeam = teamService.save(createdTeam);

        Assert.assertEquals(createdTeam.getName(), saveTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), saveTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), saveTeam.getApplicationDate());
    }

    @Test
    public void findTeamTest() {
        TeamService teamService = new TeamService(teamRepository);
        Team createdTeam = new TeamEntity();
        Team team = teamService.save(createdTeam);

        Team getTeam = teamService.find(team.getId());

        Assert.assertEquals(createdTeam.getName(), getTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), getTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), getTeam.getApplicationDate());
    }

    @Test
    public void getAllTeamsTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);

        Team[] teamArray = new Team[3];

        for (int i = 0; i < 3; i++) {
            teamArray[i] = teamService.save(createTeam());
        }

        List<Team> teamList = teamService.get();

        Assert.assertThat(teamList, IsIterableContainingInAnyOrder.containsInAnyOrder(teamArray));
    }

    private Team createTeam() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        return teamEntity;
    }
}
