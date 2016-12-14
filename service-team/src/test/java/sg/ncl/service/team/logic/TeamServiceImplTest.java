package sg.ncl.service.team.logic;

import org.apache.commons.lang3.RandomStringUtils;
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
    public void testCreateTeamTooShortName() {
        final TeamInfo teamInfo = new TeamInfo("id", "a", "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(InvalidTeamNameException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamNameStartingWithDash() {
        final TeamInfo teamInfo = new TeamInfo("id", "-a", "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(InvalidTeamNameException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamTooLongName() {
        final TeamInfo teamInfo = new TeamInfo("id", "qwertyuiopasdfg", "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(InvalidTeamNameException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamNameHasWhiteSpace() {
        final TeamInfo teamInfo = new TeamInfo("id", "ncl ncl", "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(InvalidTeamNameException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamNameHasSpecialCharacters() {
        final TeamInfo teamInfo = new TeamInfo("id", "ncl#ncl", "description", "website", "organisationType", null, null, null, null, null, members);
        exception.expect(InvalidTeamNameException.class);
        teamService.createTeam(teamInfo);
    }

    @Test
    public void testCreateTeamGoodName() {
        final TeamInfo teamInfo = new TeamInfo("id", "ncl-team", "description", "website", "organisationType", null, null, null, null, null, members);
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
    public void testCreateTeamGoodName2() {
        final TeamInfo teamInfo = new TeamInfo("id", "n-", "description", "website", "organisationType", null, null, null, null, null, members);
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
    public void testRemoveTeamEmptyId() {
        exception.expect(TeamNotFoundException.class);
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
    public void testGetTeamByGoodName() {
        teamService.getTeamByName(teamEntity.getName());
        verify(teamRepository, times(1)).findByName(anyString());
    }

    @Test
    public void testUpdateTeamEmptyId() {
        exception.expect(TeamNotFoundException.class);
        teamService.updateTeam("", teamEntity);
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
    public void testAddMemberEmptyId() {
        exception.expect(TeamNotFoundException.class);
        teamService.addMember("", teamMemberInfo);
    }

    @Test
    public void testAddMemberUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.addMember(teamEntity.getId(), teamMemberInfo);
    }

    @Test
    public void testAddMemberKnownId() {
        TeamEntity team2 = Util.getTeamEntityWithId();
        team2.addMember(teamMemberInfo);

        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(team2);

        teamService.addMember(teamEntity.getId(), teamMemberInfo);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    public void testRemoveMemberEmptyId() {
        exception.expect(TeamNotFoundException.class);
        teamService.removeMember("", teamMemberInfo);
    }

    @Test
    public void testRemoveMemberUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.removeMember(teamEntity.getId(), teamMemberInfo);
    }

    @Test
    public void testRemoveMemberKnownId() {
        TeamEntity team2 = teamEntity;
        teamEntity.addMember(teamMemberInfo);
        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(team2);
        teamService.removeMember(teamEntity.getId(), teamMemberInfo);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    public void testIsOwnerEmptyUserId() {
        when(teamRepository.findOne(anyString())).thenReturn(teamEntity);
        assertFalse(teamService.isOwner(teamEntity.getId(), ""));
    }

    @Test
    public void testIsOwnerEmptyTeamId() {
        exception.expect(TeamNotFoundException.class);
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
    public void testUpdateMemberStatusEmptyUserId() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.MEMBER);
        entity.addMember(member);
        when(teamRepository.findOne(anyString())).thenReturn(entity);
        exception.expect(TeamMemberNotFoundException.class);
        teamService.updateMemberStatus(teamEntity.getId(), "", MemberStatus.PENDING);
    }

    @Test
    public void testUpdateMemberStatusEmptyTeamId() {
        exception.expect(TeamNotFoundException.class);
        teamService.updateMemberStatus("", teamMemberInfo.getUserId(), MemberStatus.PENDING);
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
    public void testUpdateTeamStatusEmptyId() {
        exception.expect(TeamNotFoundException.class);
        teamService.updateTeamStatus("", TeamStatus.PENDING);
    }

    // case RESTRICTED
    @Test
    public void testUpdateTeamStatusCaseRestricted() {
        TeamEntity entity = Util.getTeamEntityWithId();
        entity.setStatus(TeamStatus.APPROVED);

        when(teamRepository.findOne(anyString())).thenReturn(entity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(entity);

        Team team = teamService.updateTeamStatus("id", TeamStatus.RESTRICTED);

        assertThat(team.getStatus()).isEqualTo(TeamStatus.RESTRICTED);
    }

    // case RESTRICTED- else branch, throw InvalidStatusTransitionException
    @Test
    public void testUpdateTeamStatusCaseRestrictedElseBranch() {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity entity = Util.getTeamEntityWithId();
        entity.setStatus(TeamStatus.CLOSED);

        exception.expect(InvalidStatusTransitionException.class);

        when(teamRepository.findOne(anyString())).thenReturn(entity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(entity);
        teamService.updateTeamStatus(randomIdForTest, TeamStatus.RESTRICTED);

        verify(teamRepository, times(1)).findOne(anyString());
    }

    @Test
    public void testUpdateMemberStatusUnknownId() {
        when(teamRepository.findOne(anyString())).thenReturn(null);
        exception.expect(TeamNotFoundException.class);
        teamService.updateTeamStatus(teamEntity.getId(), TeamStatus.PENDING);
    }

    // case APPROVED
    @Test
    public void testUpdateMemberStatusTrueTeamOwner() {
        TeamEntity entity = Util.getTeamEntityWithId();
        TeamMember member = Util.getTeamMemberInfo(MemberType.OWNER);
        entity.addMember(member);

        TeamEntity updatedEntity = entity;
        updatedEntity.setStatus(TeamStatus.PENDING);

        when(teamRepository.findOne(anyString())).thenReturn(entity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(updatedEntity);

        Team team = teamService.updateTeamStatus(teamEntity.getId(), TeamStatus.APPROVED);

        assertThat(team.getStatus()).isEqualTo(TeamStatus.APPROVED);
    }

    // case APPROVED- else branch, throw InvalidStatusTransitionException
    @Test
    public void testUpdateTeamStatusCaseApprovedElseBranch() {
        String randomIdForTest = RandomStringUtils.randomAlphanumeric(20);
        TeamEntity entity = Util.getTeamEntityWithId();
        entity.setStatus(TeamStatus.CLOSED);

        exception.expect(InvalidStatusTransitionException.class);

        when(teamRepository.findOne(anyString())).thenReturn(entity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(entity);
        teamService.updateTeamStatus(randomIdForTest, TeamStatus.APPROVED);

        verify(teamRepository, times(1)).findOne(anyString());
    }

    // case CLOSED
    @Test
    public void testUpdateTeamStatusCaseClosed() {
        TeamEntity entity = Util.getTeamEntityWithId();
        entity.setStatus(TeamStatus.CLOSED);

        when(teamRepository.findOne(anyString())).thenReturn(entity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(entity);

        Team team = teamService.updateTeamStatus("id", TeamStatus.CLOSED);

        assertThat(team.getStatus()).isEqualTo(TeamStatus.CLOSED);
    }

    // case default
    @Test
    public void testUpdateTeamStatusCaseDefault() {
        TeamEntity entity = Util.getTeamEntityWithId();
        entity.setStatus(TeamStatus.PENDING);

        exception.expect(InvalidTeamStatusException.class);

        when(teamRepository.findOne(anyString())).thenReturn(entity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(entity);

        teamService.updateTeamStatus("id", TeamStatus.PENDING);

        verify(teamRepository, times(1)).findOne(anyString());

    }

}
