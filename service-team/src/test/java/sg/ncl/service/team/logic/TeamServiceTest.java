package sg.ncl.service.team.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.service.team.AbstractTest;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.domain.MemberStatus;
import sg.ncl.service.team.domain.MemberType;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamMember;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.team.domain.TeamVisibility;
import sg.ncl.service.team.exceptions.TeamMemberNotFoundException;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.web.TeamMemberInfo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by Desmond/Te Ye
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class TeamServiceTest extends AbstractTest {

    @Inject
    private TeamService teamService;

    @Test
    public void testSaveTeam() {
        TeamEntity createdTeam = Util.getTeamEntity();
        Team saveTeam = teamService.createTeam(createdTeam);

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
        Team team = teamService.createTeam(createdTeam);

        Team getTeam = teamService.getTeamById(team.getId());

        Assert.assertEquals(createdTeam.getName(), getTeam.getName());
        Assert.assertEquals(createdTeam.getDescription(), getTeam.getDescription());
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        List<? extends Team> list = teamService.getAllTeams();
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void testGetTeamWithNullId() throws Exception {
        Team team = teamService.getTeamById(null);
        Assert.assertTrue(team == null);
    }

    @Test
    public void testGetTeamWithEmptyId() throws Exception {
        Team team = teamService.getTeamById("");
        Assert.assertTrue(team == null);
    }

    public void testGetTeamWithEmptyName() throws Exception {
        Assert.assertNull(teamService.getTeamByName(""));
    }

    @Test
    public void testGetTeamWithValidName() throws Exception {
        TeamEntity createdTeam = Util.getTeamEntity();
        Team team = teamService.createTeam(createdTeam);

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
            teamInfoList.add(teamService.createTeam(Util.getTeamEntity()));
        }

        List<? extends Team> resultTeamList = teamService.getAllTeams();

        Assert.assertTrue(isListTeamEqual(resultTeamList, teamInfoList));
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

        Team teamFromDb = teamService.getTeamById(id);
        Assert.assertEquals(teamFromDb.getId(), id);
        Assert.assertEquals(teamFromDb.getDescription(), description);
        Assert.assertEquals(teamFromDb.getVisibility(), TeamVisibility.PRIVATE);
    }

    @Test(expected = TeamNotFoundException.class)
    public void testUpdateTeamEmptyId() throws Exception {
        TeamEntity teamEntity = Util.getTeamEntity();
        teamEntity.setName(RandomStringUtils.randomAlphanumeric(10));
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
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.MEMBER);
        Team createdTeam = teamService.createTeam(team);

        // get team id from newly saved team
        String id = createdTeam.getId();

        teamService.addMember(id, teamMemberInfo);

        // find the team and check if user is in it
        Team teamFromDb = teamService.getTeamById(id);
        Assert.assertEquals(teamFromDb.getMembers().get(0).getUserId(), teamMemberInfo.getUserId());
    }

    @Test
    public void testIsTeamOwnerGood() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.OWNER);
        Team createdTeam = teamService.createTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();
        teamService.addMember(teamId, teamMemberInfo);

        Assert.assertThat(teamService.isOwner(teamId, userId), is(true));
    }

    @Test
    public void testIsTeamOwnerBad() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.MEMBER);
        Team createdTeam = teamService.createTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();
        teamService.addMember(teamId, teamMemberInfo);

        Assert.assertThat(teamService.isOwner(teamId, userId), is(false));
    }

    @Test
    public void changeTeamMemberStatusGood() throws Exception {
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.MEMBER);
        Team createdTeam = teamService.createTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();

        teamService.addMember(teamId, teamMemberInfo);

        TeamMember result = teamService.updateMemberStatus(teamId, userId, MemberStatus.APPROVED);

        Assert.assertThat(result.getUserId(), is(userId));
        Assert.assertThat(result.getMemberType(), is(teamMemberInfo.getMemberType()));
        Assert.assertThat(result.getMemberStatus(), is(MemberStatus.APPROVED));
    }

    @Test(expected = TeamMemberNotFoundException.class)
    public void changeTeamMemberStatusBad() throws Exception {
        // no add team member for this test case
        TeamEntity team = Util.getTeamEntity();
        TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.MEMBER);
        Team createdTeam = teamService.createTeam(team);

        String userId = teamMemberInfo.getUserId();
        String teamId = createdTeam.getId();

        // should throw error here
        TeamMember result = teamService.updateMemberStatus(teamId, userId, MemberStatus.APPROVED);
    }

    @Test(expected = TeamNotFoundException.class)
    public void changeTeamStatusNoSuchTeam() throws Exception {
        teamService.updateTeamStatus(RandomStringUtils.randomAlphanumeric(20), TeamStatus.APPROVED);
    }

    @Test
    public void changeTeamStatusGood() throws Exception {
        Team one = Util.getTeamEntity();
        TeamMemberInfo owner = Util.getTeamMemberInfo(MemberType.OWNER);
        Team createdTeam = teamService.createTeam(one);
        teamService.addMember(createdTeam.getId(), owner);
        Team approvedTeam = teamService.updateTeamStatus(createdTeam.getId(), TeamStatus.APPROVED);

        Assert.assertThat(approvedTeam.getId(), is(createdTeam.getId()));
        Assert.assertThat(approvedTeam.getStatus(), is(TeamStatus.APPROVED));
        Assert.assertThat(approvedTeam.getMembers().size(), is(1));
    }

    @Test(expected = TeamNotFoundException.class)
    public void removeTeamNotFound() throws Exception {
        teamService.removeTeam(RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
    //(expected = TeamNotFoundException.class)
    public void removeTeamGood() throws Exception {
        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.createTeam(one);
        teamService.removeTeam(createdTeam.getId());

        // expect a team not found exception here when retrieving since we already remove the team previously
        Team team = teamService.getTeamById(createdTeam.getId());
        Assert.assertTrue(team == null);
    }

    @Test(expected = TeamNotFoundException.class)
    public void removeTeamMemberTeamNotFound() throws Exception {
        teamService.removeMember(RandomStringUtils.randomAlphanumeric(20), Util.getTeamMemberInfo(MemberType.MEMBER));
    }

    @Test
    public void removeTeamMemberGood() throws Exception {
        Team one = Util.getTeamEntity();
        Team createdTeam = teamService.createTeam(one);
        TeamMemberInfo teamMemberOne = Util.getTeamMemberInfo(MemberType.OWNER);
        TeamMemberInfo teamMemberTwo = Util.getTeamMemberInfo(MemberType.MEMBER);

        teamService.addMember(createdTeam.getId(), teamMemberOne);
        teamService.addMember(createdTeam.getId(), teamMemberTwo);

        Team result = teamService.removeMember(createdTeam.getId(), teamMemberTwo);
        List<? extends TeamMember> membersList = result.getMembers();

        for (TeamMember member : membersList) {
            if (member.getUserId().equals(teamMemberOne.getUserId())) {
                Assert.assertThat(member.getMemberStatus(), is(MemberStatus.PENDING));
            } else if (member.getUserId().equals(teamMemberTwo.getUserId())) {
                Assert.assertThat(member.getMemberStatus(), is(MemberStatus.REJECTED));
            } else {
                Assert.fail();
            }
        }
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
