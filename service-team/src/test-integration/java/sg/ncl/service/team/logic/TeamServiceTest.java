package sg.ncl.service.team.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.web.TeamMemberInfo;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamNameNullException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmond/Te Ye
 */
public class TeamServiceTest extends AbstractTest {

    @Inject
    private TeamService teamService;

    @Test
    public void testSaveTeam() {
        TeamEntity createdTeam = Util.getTeamEntity();
        Team saveTeam = teamService.createTeam(createdTeam);

        Assert.assertEquals(createdTeam.getName(), saveTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), saveTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), saveTeam.getApplicationDate());
    }

    @Test(expected = TeamNotFoundException.class)
    public void testFindTeamWithNoTeamsInDb() throws Exception {
        teamService.getById(RandomStringUtils.randomAlphabetic(20));
    }

    @Test
    public void testFindTeam() {
        TeamEntity createdTeam = new TeamEntity();
        Team team = teamService.createTeam(createdTeam);

        Team getTeam = teamService.getById(team.getId());

        Assert.assertEquals(createdTeam.getName(), getTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), getTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), getTeam.getApplicationDate());
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        List<Team> list = teamService.getAll();
        Assert.assertTrue(list.size() == 0);
    }

    @Test(expected = TeamIdNullException.class)
    public void testGetTeamWithNullId() throws Exception {
        teamService.getById(null);
    }

    @Test(expected = TeamIdNullException.class)
    public void testGetTeamWithEmptyId() throws Exception {
        teamService.getById("");
    }

    @Test(expected = TeamNameNullException.class)
    public void testGetTeamWithNullName() throws Exception {
        teamService.getByName(null);
    }

    @Test(expected = TeamNameNullException.class)
    public void testGetTeamWithEmptyName() throws Exception {
        teamService.getByName("");
    }

    @Test
    public void testGetTeamWithValidName() throws Exception {
        TeamEntity createdTeam = Util.getTeamEntity();
        Team team = teamService.createTeam(createdTeam);

        Team resultTeam = teamService.getByName(team.getName());

        Assert.assertEquals(createdTeam.getName(), resultTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), resultTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), resultTeam.getApplicationDate());
    }

    @Test
    public void testGetAllTeams() throws Exception {
        List<Team> teamInfoList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            teamInfoList.add(teamService.createTeam(Util.getTeamEntity()));
        }

        List<Team> resultTeamList = teamService.getAll();

        Assert.assertTrue(isListTeamEqual(resultTeamList, teamInfoList));
    }

    @Test
    public void testGetValidTeamStatus() {
        TeamEntity createdTeam = new TeamEntity();
        Team team = teamService.createTeam(createdTeam);

        String resultTeamStatus = teamService.getTeamStatus(team.getId());
        Assert.assertEquals(resultTeamStatus, TeamStatus.PENDING.toString());
    }

    @Test
    public void testUpdateTeamInfo() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        Team createdTeam = teamService.createTeam(teamEntity);
        final String id = createdTeam.getId();

        String description = RandomStringUtils.randomAlphanumeric(20);
        teamEntity.setDescription(description);
        teamEntity.setVisibility(TeamVisibility.PRIVATE);

        teamService.updateTeam(id, teamEntity);

        Team teamFromDb = teamService.getById(id);
        Assert.assertEquals(teamFromDb.getId(), id);
        Assert.assertEquals(teamFromDb.getDescription(), description);
        Assert.assertEquals(teamFromDb.getVisibility(), TeamVisibility.PRIVATE);
    }

    @Test(expected = TeamIdNullException.class)
    public void testUpdateTeamNullId() throws Exception {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(null);
        teamService.updateTeam(null, teamEntity);
    }

    @Test(expected = TeamIdNullException.class)
    public void testUpdateTeamEmptyId() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setName(RandomStringUtils.randomAlphanumeric(20));
        Team team = teamService.createTeam(teamEntity);
        teamService.updateTeam("", team);
    }

    @Test
    public void testUpdateTeamNullField() {
        TeamEntity team = Util.getTeamEntity();
        Team createdTeam = teamService.createTeam(team);
        final String id = createdTeam.getId();

        team.setDescription(null);

        // test should pass
        teamService.updateTeam(id, team);
    }

    @Test
    public void testAddUserToTeam() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo();
        Team createdTeam = teamService.createTeam(team);

        // get team id from newly saved team
        String id = createdTeam.getId();

        teamService.addUserToTeam(id, teamMemberInfo);

        // find the team and check if user is in it
        Team teamFromDb = teamService.getById(id);
        Assert.assertEquals(teamFromDb.getMembers().get(0).getUserId(), teamMemberInfo.getUserId());
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

    private boolean isListTeamEqual(List<Team> one, List<Team> two) {
        ArrayList<Team> cp = new ArrayList<>(one);
        for (Team twoIterator : two) {
            if (!cp.remove(twoIterator)) {
                return false;
            }
        }
        return cp.isEmpty();
    }
}
