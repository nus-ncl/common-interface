package sg.ncl.service.team.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.TeamIdNullException;
import sg.ncl.service.team.exceptions.TeamMemberNotFoundException;
import sg.ncl.service.team.exceptions.TeamNameNullException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.web.TeamMemberInfo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by Desmond/Te Ye
 */
public class TeamServiceTest extends AbstractTest {

    @Inject
    private TeamService teamService;

    @Test
    public void testSaveTeam() {
        TeamEntity createdTeam = Util.getTeamEntity();
        Team saveTeam = teamService.addTeam(createdTeam);

        Assert.assertEquals(createdTeam.getName(), saveTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), saveTeam.getDescription());
        Assert.assertEquals(createdTeam.getApplicationDate(), saveTeam.getApplicationDate());
    }

    @Test(expected = TeamNotFoundException.class)
    public void testFindTeamWithNoTeamsInDb() throws Exception {
        teamService.getTeamById(RandomStringUtils.randomAlphabetic(20));
    }

    @Test
    public void testFindTeam() {
        TeamEntity createdTeam = new TeamEntity();
        Team team = teamService.addTeam(createdTeam);

        Team getTeam = teamService.getTeamById(team.getId());

        Assert.assertEquals(createdTeam.getName(), getTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), getTeam.getDescription());
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        List<? extends Team> list = teamService.getTeams();
        Assert.assertTrue(list.size() == 0);
    }

    @Test(expected = TeamIdNullException.class)
    public void testGetTeamWithNullId() throws Exception {
        teamService.getTeamById(null);
    }

    @Test(expected = TeamIdNullException.class)
    public void testGetTeamWithEmptyId() throws Exception {
        teamService.getTeamById("");
    }

    @Test(expected = TeamNameNullException.class)
    public void testGetTeamWithNullName() throws Exception {
        teamService.getTeamByName(null);
    }

    @Test(expected = TeamNameNullException.class)
    public void testGetTeamWithEmptyName() throws Exception {
        teamService.getTeamByName("");
    }

    @Test
    public void testGetTeamWithValidName() throws Exception {
        TeamEntity createdTeam = Util.getTeamEntity();
        Team team = teamService.addTeam(createdTeam);

        Team resultTeam = teamService.getTeamByName(team.getName());

        Assert.assertEquals(createdTeam.getName(), resultTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), resultTeam.getDescription());

        Assert.assertNotNull(resultTeam.getId());
        Assert.assertNotNull(resultTeam.getApplicationDate());
    }

    @Test
    public void testGetAllTeams() throws Exception {
        List<Team> teamInfoList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            teamInfoList.add(teamService.addTeam(Util.getTeamEntity()));
        }

        List<? extends Team> resultTeamList = teamService.getTeams();

        Assert.assertTrue(isListTeamEqual(resultTeamList, teamInfoList));
    }

    @Test
    public void testUpdateTeamInfo() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(teamEntity);
        final String id = createdTeam.getId();

        String description = RandomStringUtils.randomAlphanumeric(20);
        teamEntity.setDescription(description);
        teamEntity.setVisibility(TeamVisibility.PRIVATE);

        teamService.updateTeam(id, teamEntity);

        Team teamFromDb = teamService.getTeamById(id);
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
        Team team = teamService.addTeam(teamEntity);
        teamService.updateTeam("", team);
    }

    @Test
    public void testUpdateTeamNullField() {
        TeamEntity team = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(team);
        final String id = createdTeam.getId();

        team.setDescription(null);

        // test should pass
        teamService.updateTeam(id, team);
    }

    @Test
    public void testAddUserToTeam() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(TeamMemberType.MEMBER);
        Team createdTeam = teamService.addTeam(team);

        // get team id from newly saved team
        String id = createdTeam.getId();

        teamService.addTeamMember(id, teamMemberInfo);

        // find the team and check if user is in it
        Team teamFromDb = teamService.getTeamById(id);
        Assert.assertEquals(teamFromDb.getMembers().get(0).getUserId(), teamMemberInfo.getUserId());
    }

    @Test
    public void testIsTeamOwnerGood() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(TeamMemberType.OWNER);
        Team createdTeam = teamService.addTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();
        teamService.addTeamMember(teamId, teamMemberInfo);

        Assert.assertThat(teamService.isTeamOwner(userId, teamId), is(true));
    }

    @Test
    public void testIsTeamOwnerBad() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(TeamMemberType.MEMBER);
        Team createdTeam = teamService.addTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();
        teamService.addTeamMember(teamId, teamMemberInfo);

        Assert.assertThat(teamService.isTeamOwner(userId, teamId), is(false));
    }

    @Test
    public void changeTeamMemberStatusGood() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(TeamMemberType.MEMBER);
        Team createdTeam = teamService.addTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();

        teamService.addTeamMember(teamId, teamMemberInfo);

        TeamMember result = teamService.changeTeamMemberStatus(userId, teamId, TeamMemberStatus.APPROVED);

        Assert.assertThat(result.getUserId(), is(userId));
        Assert.assertThat(result.getMemberType(), is(teamMemberInfo.getMemberType()));
        Assert.assertThat(result.getMemberStatus(), is(TeamMemberStatus.APPROVED));
    }

    @Test(expected = TeamMemberNotFoundException.class)
    public void changeTeamMemberStatusBad() throws Exception {
        // no add team member for this test case
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(TeamMemberType.MEMBER);
        Team createdTeam = teamService.addTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();

        // should throw error here
        TeamMember result = teamService.changeTeamMemberStatus(userId, teamId, TeamMemberStatus.APPROVED);
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

    private boolean isListTeamEqual(List<? extends Team> one, List<Team> two) {
        ArrayList<Team> cp = new ArrayList<>(one);
        for (Team twoIterator : two) {
            if (!cp.remove(twoIterator)) {
                return false;
            }
        }
        return cp.isEmpty();
    }
}
