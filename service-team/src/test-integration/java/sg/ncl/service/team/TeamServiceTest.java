package sg.ncl.service.team;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.entities.TeamMemberEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

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

    @Test(expected = TeamNotFoundException.class)
    public void findTeamWithNoTeamsInDbTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        teamService.find(RandomStringUtils.randomAlphabetic(20));
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
    public void getAllTeamsWithNoUserInDbTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        List<Team> list = teamService.get();
        Assert.assertTrue(list.size() == 0);
    }

    @Test(expected = TeamIdNullException.class)
    public void getUserWithNullIdTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        teamService.find(null);
    }

    @Test(expected = TeamIdNullException.class)
    public void getUserWithEmptyIdTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        teamService.find("");
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

    @Test
    public void getValidTeamStatusTest() {
        TeamService teamService = new TeamService(teamRepository);
        Team createdTeam = new TeamEntity();
        Team team = teamService.save(createdTeam);

        String resultTeamStatus = teamService.getTeamStatus(team.getId());
        Assert.assertEquals(resultTeamStatus, TeamStatus.PENDING.toString());
    }

    @Test
    public void updateTeamInfoTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        Team team = createTeam();
        team = teamService.save(team);
        final String idString = team.getId();

        // get team and store the original description from database
        TeamEntity originalTeamEntity = teamService.find(idString);
        final String originalDescription = originalTeamEntity.getDescription();

        // change description and put
        String modifiedDescription = RandomStringUtils.randomAlphabetic(20);
        originalTeamEntity.setDescription(modifiedDescription);

        teamService.update(idString, originalTeamEntity);

        originalTeamEntity = teamService.find(idString);
        Assert.assertEquals(originalTeamEntity.getId(), idString);
        Assert.assertNotEquals(originalTeamEntity.getDescription(), originalDescription);
        Assert.assertEquals(originalTeamEntity.getDescription(), modifiedDescription);
    }

    @Test(expected = TeamIdNullException.class)
    public void updateTeamNullIdTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity teamEntity = new TeamEntity();
        teamService.update(null, teamEntity);
    }

    @Test(expected = TeamIdNullException.class)
    public void updateTeamEmptyIdTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity teamEntity = new TeamEntity();
        teamService.update("", teamEntity);
    }

    @Test
    public void updateTeamNullFieldTest() {
        TeamService teamService = new TeamService(teamRepository);
        Team team = createTeam();
        team = teamService.save(team);
        final String idString = team.getId();

        // get team and store the original description from database
        TeamEntity originalTeamEntity = teamService.find(idString);
        final String originalDescription = originalTeamEntity.getDescription();

        // change description and put
        originalTeamEntity.setDescription(null);

        // test should pass
        teamService.update(idString, originalTeamEntity);
    }

    @Test
    public void addUserToTeamTest() throws  Exception {
        TeamService teamService = new TeamService(teamRepository);
        Team team = createTeam();
        team = teamService.save(team);
        String teamId = team.getId();
        String userId = RandomStringUtils.randomAlphabetic(20);
        TeamEntity teamEntity = teamService.find(teamId);
        teamEntity.addMember(userId);
        teamService.update(teamId, teamEntity);

        TeamEntity teamEntityFromDb = teamService.find(teamId);
        List<TeamMemberEntity> teamList = teamEntityFromDb.getMembers();
        Assert.assertEquals(teamList.get(0).getUserId(), userId);
    }

    private Team createTeam() {
        TeamEntity teamEntity = new TeamEntity();

        teamEntity.setName(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setDescription(RandomStringUtils.randomAlphabetic(20));
        teamEntity.setApplicationDate(ZonedDateTime.now());

        return teamEntity;
    }
}
