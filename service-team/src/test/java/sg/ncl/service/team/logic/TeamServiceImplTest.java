package sg.ncl.service.team.logic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.service.team.Util;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.*;
import sg.ncl.service.team.web.TeamInfo;
import sg.ncl.service.team.web.TeamMemberInfo;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author James Ng
 */
public class TeamServiceImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private TeamRepository teamRepository;

    private TeamService teamService;
    private List<TeamMember> members = new ArrayList<>();
    private TeamEntity teamEntity = Util.getTeamEntityWithId();
    private TeamMemberInfo teamMemberInfo = Util.getTeamMemberInfo(MemberType.MEMBER);

    @Before
    public void before() {
        assertThat(mockingDetails(teamRepository).isMock()).isTrue();
        teamService = new TeamServiceImpl(teamRepository);
    }

    @Test
    public void testCreateTeamNullTeam() {
        final TeamInfo teamInfo = null;
        exception.expect(IllegalArgumentException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamNullName() {
        final TeamInfo teamInfo = new TeamInfo("id", null, "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(TeamNameNullOrEmptyException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamEmptyName() {
        final TeamInfo teamInfo = new TeamInfo("id", "", "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(TeamNameNullOrEmptyException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamGoodName() {
        final TeamInfo teamInfo = new TeamInfo("id", "name", "description", "website", "organisationType", null, null, null, null, null, members);
        when(teamRepository.save(any(TeamEntity.class))).thenAnswer(i -> i.getArgumentAt(0, TeamEntity.class));
        final Team team = teamService.createTeam(teamInfo);

        verify(teamRepository, times(1)).save(any(TeamEntity.class));
//        assertThat(team.getId()).isEqualTo(teamInfo.getId());
        assertThat(team.getName()).isEqualTo(teamInfo.getName());
        assertThat(team.getDescription()).isEqualTo(teamInfo.getDescription());
        assertThat(team.getWebsite()).isEqualTo(teamInfo.getWebsite());
        assertThat(team.getOrganisationType()).isEqualTo(teamInfo.getOrganisationType());
    }

    @Test
    public void testRemoveTeamNullId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.removeTeam(null);
    }

    @Test
    public void testRemoveTeamEmptyId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.removeTeam("");
    }

    @Test
    public void testRemoveTeamUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.removeTeam(teamEntity.getId());
    }

    @Test
    public void testRemoveTeamKnownId() {
        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        teamService.removeTeam(teamEntity.getId());
        verify(teamRepository, times(1)).delete(anyString());
    }

    @Test
    public void testGetAllTeams() {
        teamService.getAllTeams();
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    public void testGetTeamsByVisibility() {
        teamService.getTeamsByVisibility(TeamVisibility.PUBLIC);
        verify(teamRepository, times(1)).findByVisibility(any(TeamVisibility.class));
    }

    @Test
    public void testGetTeamById() {
        teamService.getTeamById(teamEntity.getId());
        verify(teamRepository, times(1)).findOne(anyString());
    }

    @Test
    public void testGetTeamByNullName() {
        exception.expect(TeamNameNullOrEmptyException.class);
        teamService.getTeamByName(null);
    }

    @Test
    public void testGetTeamByEmptyName() {
        exception.expect(TeamNameNullOrEmptyException.class);
        teamService.getTeamByName("");
    }

    @Test
    public void testGetTeamByGoodName() {
        teamService.getTeamByName(teamEntity.getName());
        verify(teamRepository, times(1)).findByName(anyString());
    }

    @Test
    public void testUpdateTeamNullId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.updateTeam(null, teamEntity);
    }

    @Test
    public void testUpdateTeamEmptyId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.updateTeam("", teamEntity);
    }

    @Test
    public void testUpdateTeamNullTeam() {
        exception.expect(IllegalArgumentException.class);
        teamService.updateTeam(teamEntity.getId(), null);
    }

    @Test
    public void testUpdateTeamUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.updateTeam(teamEntity.getId(), teamEntity);
    }

    @Test
    public void testUpdateTeamKnownId() {
        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenAnswer(i -> i.getArgumentAt(0, TeamEntity.class));
        final Team team = teamService.updateTeam(teamEntity.getId(), teamEntity);

        verify(teamRepository, times(1)).save(any(TeamEntity.class));
        assertThat(team.getDescription()).isEqualTo(teamEntity.getDescription());
        assertThat(team.getPrivacy()).isEqualTo(teamEntity.getPrivacy());
        assertThat(team.getStatus()).isEqualTo(teamEntity.getStatus());
        assertThat(team.getVisibility()).isEqualTo(teamEntity.getVisibility());
        assertThat(team.getWebsite()).isEqualTo(teamEntity.getWebsite());
    }

    @Test
    public void testAddMemberNullId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.addMember(null, teamMemberInfo);
    }

    @Test
    public void testAddMemberEmptyId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.addMember("", teamMemberInfo);
    }

    @Test
    public void testAddMemberNullTeamMember() {
        exception.expect(IllegalArgumentException.class);
        teamService.addMember(teamEntity.getId(), null);
    }

    @Test
    public void testAddMemberUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.addMember(teamEntity.getId(), teamMemberInfo);
    }

    @Test
    public void testAddMemberKnownId() {
        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        teamService.addMember(teamEntity.getId(), teamMemberInfo);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    public void testRemoveMemberNullId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.removeMember(null, teamMemberInfo);
    }

    @Test
    public void testRemoveMemberEmptyId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.removeMember("", teamMemberInfo);
    }

    @Test
    public void testRemoveMemberNullTeamMember() {
        exception.expect(IllegalArgumentException.class);
        teamService.removeMember(teamEntity.getId(), null);
    }

    @Test
    public void testRemoveMemberUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.removeMember(teamEntity.getId(), teamMemberInfo);
    }

    @Test
    public void testRemoveMemberKnownId() {
        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        teamService.removeMember(teamEntity.getId(), teamMemberInfo);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    public void testIsOwnerNullUserId() {
        exception.expect(UserIdNullOrEmptyException.class);
        teamService.isOwner(teamEntity.getId(), null);
    }

    @Test
    public void testIsOwnerEmptyUserId() {
        exception.expect(UserIdNullOrEmptyException.class);
        teamService.isOwner(teamEntity.getId(), "");
    }

    @Test
    public void testIsOwnerNullTeamId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.isOwner(null, teamMemberInfo.getUserId());
    }

    @Test
    public void testIsOwnerEmptyTeamId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.isOwner("", teamMemberInfo.getUserId());
    }

    @Test
    public void testIsOwnerUnknownTeamId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.isOwner(teamEntity.getId(), teamMemberInfo.getUserId());
    }

    @Test
    public void testIsOwnerTrue() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.OWNER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        Boolean flag = teamService.isOwner(teamEntity.getId(), member.getUserId());
        assertTrue(flag);
    }

    @Test
    public void testIsOwnerFalse() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.MEMBER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        Boolean flag = teamService.isOwner(teamEntity.getId(), member.getUserId());
        assertFalse(flag);
    }

    @Test
    public void testUpdateMemberStatusNullUserId() {
        exception.expect(UserIdNullOrEmptyException.class);
        teamService.updateMemberStatus(teamEntity.getId(), null, MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusEmptyUserId() {
        exception.expect(UserIdNullOrEmptyException.class);
        teamService.updateMemberStatus(teamEntity.getId(), "", MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusNullTeamId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.updateMemberStatus(null, teamMemberInfo.getUserId(), MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusEmptyTeamId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.updateMemberStatus("", teamMemberInfo.getUserId(), MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusNullStatus() {
        exception.expect(IllegalArgumentException.class);
        teamService.updateMemberStatus(teamEntity.getId(), teamMemberInfo.getUserId(), null);
    }

    @Test
    public void testUpdateMemberStatusUnknownTeamId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.updateMemberStatus(teamEntity.getId(), teamMemberInfo.getUserId(), MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusUnknownUserId() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.MEMBER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        exception.expect(TeamMemberNotFoundException.class);
        teamService.updateMemberStatus(teamEntity.getId(), teamMemberInfo.getUserId(), MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusKnownUserId() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.MEMBER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        teamService.updateMemberStatus(entity.getId(), member.getUserId(), MemberStatus.PENDING);
    }

    @Test
    public void testUpdateTeamStatusNullId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.updateTeamStatus(null, TeamStatus.PENDING);
    }

    @Test
    public void testUpdateTeamStatusEmptyId() {
        exception.expect(TeamIdNullOrEmptyException.class);
        teamService.updateTeamStatus("", TeamStatus.PENDING);
    }

    @Test
    public void testUpdateTeamStatusNullStatus() {
        exception.expect(IllegalArgumentException.class);
        teamService.updateTeamStatus(teamEntity.getId(), null);
    }

    @Test
    public void testUpdateMemberStatusUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.updateTeamStatus(teamEntity.getId(), TeamStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusFalseTeamOwner() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.MEMBER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        exception.expect(NoOwnerInTeamException.class);
        teamService.updateTeamStatus(teamEntity.getId(), TeamStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusTrueTeamOwner() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.OWNER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        Team team = teamService.updateTeamStatus(teamEntity.getId(), TeamStatus.PENDING);
        assertThat(team.getStatus()).isEqualTo(TeamStatus.PENDING);
    }
}
