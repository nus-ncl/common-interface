package sg.ncl.service.team.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.*;
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
        Assert.assertEquals(createdTeam.getOrganisationType(), saveTeam.getOrganisationType());
        Assert.assertEquals(createdTeam.getWebsite(), saveTeam.getWebsite());
    }

    @Test
            //(expected = TeamNotFoundException.class)
    public void testFindTeamWithNoTeamsInDb() throws Exception {
        Team team = teamService.getTeamById(RandomStringUtils.randomAlphabetic(20));
        Assert.assertTrue(team == null);
    }

    @Test
    public void testFindTeam() {
        TeamEntity createdTeam = Util.getTeamEntity();
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

    @Test
            //(expected = TeamIdNullOrEmptyException.class)
    public void testGetTeamWithNullId() throws Exception {
        Team team = teamService.getTeamById(null);
        Assert.assertTrue(team == null);
    }

    @Test
            //(expected = TeamIdNullOrEmptyException.class)
    public void testGetTeamWithEmptyId() throws Exception {
        Team team = teamService.getTeamById("");
        Assert.assertTrue(team == null);
    }

    @Test(expected = TeamNameNullOrEmptyException.class)
    public void testGetTeamWithNullName() throws Exception {
        teamService.getTeamByName(null);
    }

    @Test(expected = TeamNameNullOrEmptyException.class)
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

    @Test(expected = TeamIdNullOrEmptyException.class)
    public void testUpdateTeamNullId() throws Exception {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(null);
        teamService.updateTeam(null, teamEntity);
    }

    @Test(expected = TeamIdNullOrEmptyException.class)
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

    @Test(expected = TeamIdNullOrEmptyException.class)
    public void changeTeamStatusNullTeam() throws Exception {
        teamService.changeTeamStatus(null, TeamStatus.APPROVED);
    }

    @Test(expected = TeamNotFoundException.class)
    public void changeTeamStatusNoSuchTeam() throws Exception {
        teamService.changeTeamStatus(RandomStringUtils.randomAlphanumeric(20), TeamStatus.APPROVED);
    }

    @Test(expected = NoOwnerInTeamException.class)
    public void changeTeamStatusNoOwner() throws Exception {
        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(one);
        teamService.changeTeamStatus(createdTeam.getId(), TeamStatus.APPROVED);
    }

    @Test
    public void changeTeamStatusGood() throws Exception {
        Team one = Util.getTeamEntity();
        TeamMemberInfo owner = Util.getTeamMemberInfo(TeamMemberType.OWNER);
        Team createdTeam = teamService.addTeam(one);
        teamService.addTeamMember(createdTeam.getId(), owner);
        Team approvedTeam = teamService.changeTeamStatus(createdTeam.getId(), TeamStatus.APPROVED);

        Assert.assertThat(approvedTeam.getId(), is(createdTeam.getId()));
        Assert.assertThat(approvedTeam.getStatus(), is(TeamStatus.APPROVED));
        Assert.assertThat(approvedTeam.getMembers().size(), is(1));
    }

    @Test(expected = TeamIdNullOrEmptyException.class)
    public void removeTeamNullId() throws Exception {
        teamService.removeTeam(null);
    }

    @Test(expected = TeamNotFoundException.class)
    public void removeTeamNotFound() throws Exception {
        teamService.removeTeam(RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
            //(expected = TeamNotFoundException.class)
    public void removeTeamGood() throws Exception {
        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(one);
        teamService.removeTeam(createdTeam.getId());

        // expect a team not found exception here when retrieving since we already remove the team previously
        Team team = teamService.getTeamById(createdTeam.getId());
        Assert.assertTrue(team == null);
    }

    @Test(expected = TeamIdNullOrEmptyException.class)
    public void removeTeamMemberTeamIdNull() throws Exception {
        teamService.removeTeamMember(null, Util.getTeamMemberInfo(TeamMemberType.MEMBER));
    }

    @Test(expected = TeamNotFoundException.class)
    public void removeTeamMemberTeamNotFound() throws Exception {
        teamService.removeTeamMember(RandomStringUtils.randomAlphanumeric(20), Util.getTeamMemberInfo(TeamMemberType.MEMBER));
    }

    @Test
    public void removeTeamMemberGood() throws Exception {
        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.addTeam(one);
        TeamMemberInfo teamMemberOne = Util.getTeamMemberInfo(TeamMemberType.OWNER);
        TeamMemberInfo teamMemberTwo = Util.getTeamMemberInfo(TeamMemberType.MEMBER);

        teamService.addTeamMember(createdTeam.getId(), teamMemberOne);
        teamService.addTeamMember(createdTeam.getId(), teamMemberTwo);

        Team result = teamService.removeTeamMember(createdTeam.getId(), teamMemberTwo);
        List<? extends TeamMember> membersList = result.getMembers();

        Assert.assertThat(membersList.size(), is(1));
        Assert.assertThat(membersList.get(0).getUserId(), is(teamMemberOne.getUserId()));
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
