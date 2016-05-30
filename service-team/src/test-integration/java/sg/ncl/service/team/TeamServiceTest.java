package sg.ncl.service.team;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * Created by Desmond/Te Ye
 */
public class TeamServiceTest extends AbstractTest {
    @Inject
    private TeamRepository teamRepository;

    @Test
    public void saveTeamTest() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        TeamService teamService = new TeamService(teamRepository);
        Team team = teamService.save(teamEntity);

        Assert.assertEquals(teamEntity.getName(), team.getName());
        Assert.assertEquals(teamEntity.getDescription(), team.getDescription());
        Assert.assertEquals(teamEntity.getApplicationDate(), team.getApplicationDate());
    }

    @Test
    public void findTeamTest() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        TeamService teamService = new TeamService(teamRepository);
        Team team = teamService.save(teamEntity);

        Team getTeam = teamService.find(team.getId());

        Assert.assertEquals(teamEntity.getName(), getTeam.getName());
        Assert.assertEquals(teamEntity.getDescription(), getTeam.getDescription());
        Assert.assertEquals(teamEntity.getApplicationDate(), getTeam.getApplicationDate());
    }
}
