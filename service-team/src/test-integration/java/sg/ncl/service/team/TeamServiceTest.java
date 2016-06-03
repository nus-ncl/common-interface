package sg.ncl.service.team;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
import sg.ncl.service.team.data.jpa.entities.TeamMemberEntity;
import sg.ncl.service.team.data.jpa.repositories.TeamRepository;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.dtos.TeamInfo;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import javax.inject.Inject;
import java.util.ArrayList;
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
        TeamEntity createdTeam = TeamCommon.createTeam();
        TeamEntity saveTeam = teamService.save(createdTeam);

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
        TeamEntity createdTeam = new TeamEntity();
        TeamEntity team = teamService.save(createdTeam);

        Team getTeam = teamService.find(team.getId());

        Assert.assertEquals(createdTeam.getName(), getTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), getTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), getTeam.getApplicationDate());
    }

    @Test
    public void getAllTeamsWithNoUserInDbTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        List<TeamEntity> list = teamService.get();
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

        List<TeamEntity> teamInfoList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            teamInfoList.add(teamService.save(TeamCommon.createTeam()));
        }

        List<TeamEntity> resultTeamList = teamService.get();

        Assert.assertTrue(isListEqual(resultTeamList, teamInfoList));
    }

    @Test
    public void getValidTeamStatusTest() {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity createdTeam = new TeamEntity();
        TeamEntity team = teamService.save(createdTeam);

        String resultTeamStatus = teamService.getTeamStatus(team.getId());
        Assert.assertEquals(resultTeamStatus, TeamStatus.PENDING.toString());
    }

    @Test
    public void updateTeamInfoTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity team = TeamCommon.createTeam();
        team = teamService.save(team);
        final String idString = team.getId();

        // get team and store the original description from database
        TeamEntity originalTeamEntity = teamService.find(idString);
        final String originalDescription = originalTeamEntity.getDescription();

        // change description and put
        String modifiedDescription = RandomStringUtils.randomAlphabetic(20);
        originalTeamEntity.setDescription(modifiedDescription);

        teamService.update(originalTeamEntity);

        TeamEntity teamEntityFromDb = teamService.find(idString);
        Assert.assertEquals(teamEntityFromDb.getId(), idString);
        Assert.assertNotEquals(teamEntityFromDb.getDescription(), originalDescription);
        Assert.assertEquals(teamEntityFromDb.getDescription(), modifiedDescription);
    }

    @Test(expected = TeamIdNullException.class)
    public void updateTeamNullIdTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(null);
        teamService.update(teamEntity);
    }

    @Test(expected = TeamIdNullException.class)
    public void updateTeamEmptyIdTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName("");
        teamService.update(teamEntity);
    }

    @Test
    public void updateTeamNullFieldTest() {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity team = TeamCommon.createTeam();
        team = teamService.save(team);
        final String idString = team.getId();

        // get team and store the original description from database
        TeamEntity originalTeamEntity = teamService.find(idString);
        final String originalDescription = originalTeamEntity.getDescription();

        // change description and put
        originalTeamEntity.setDescription(null);

        // test should pass
        teamService.update(originalTeamEntity);
    }

    @Test
    public void addUserToTeamTest() throws Exception {
        TeamService teamService = new TeamService(teamRepository);
        TeamEntity team = TeamCommon.createTeam();
        team = teamService.save(team);

        // get team id from newly saved team
        String teamId = team.getId();
        String userId = RandomStringUtils.randomAlphabetic(20);

        teamService.addUserToTeam(userId, teamId);

        // find the team and check if user is in it
        TeamEntity teamEntityFromDb = teamService.find(teamId);
        List<TeamMemberEntity> teamList = teamEntityFromDb.getMembers();
        Assert.assertEquals(teamList.get(0).getUserId(), userId);
    }

    private boolean isListEqual(List<TeamEntity> one, List<TeamEntity> two) {
        ArrayList<TeamEntity> cp = new ArrayList<>(one);
        for (TeamEntity twoIterator : two) {
            if (!cp.remove(twoIterator)) {
                return false;
            }
        }
        return cp.isEmpty();
    }
}
