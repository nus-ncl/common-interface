package sg.ncl.service.team.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.exception.base.UnauthorizedException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.team.data.jpa.TeamQuotaEntity;
import sg.ncl.service.team.data.jpa.TeamRepository;
import sg.ncl.service.team.domain.*;
import sg.ncl.service.team.exceptions.TeamNotFoundException;
import sg.ncl.service.team.exceptions.TeamQuotaOutOfRangeException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sg.ncl.service.team.util.TestUtil.*;

/**
 * @Author dcsyeoty, Tran Ly Vu
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TeamsController.class, secure = true)
@ContextConfiguration(classes = {TeamsController.class})
public class TeamsControllerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Inject
    private ObjectMapper mapper;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @MockBean
    private AnalyticsService analyticsService;


    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(teamService).isMock()).isTrue();
        assertThat(mockingDetails(analyticsService).isMock()).isTrue();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllTeamsWithNoUserInDb() throws Exception {
        mockMvc.perform(get(TeamsController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllTeamsForbiddenException() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(null);

        mockMvc.perform(get(TeamsController.PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll() throws Exception {
        final List<Team> list = new ArrayList<>();
        final TeamEntity entity1 = getTeamEntityWithId();
        final TeamEntity entity2 = getTeamEntityWithId();
        list.add(entity1);
        list.add(entity2);

        when(teamService.getAllTeams()).thenReturn(list);

        mockMvc.perform(get(TeamsController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity1.getId()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity1.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$[0].website", is(equalTo(entity1.getWebsite()))))
                .andExpect(jsonPath("$[0].organisationType", is(equalTo(entity1.getOrganisationType()))))
                .andExpect(jsonPath("$[0].visibility", is(equalTo(entity1.getVisibility().name()))))
                .andExpect(jsonPath("$[0].privacy", is(equalTo(entity1.getPrivacy().name()))))
                .andExpect(jsonPath("$[0].status", is(equalTo(entity1.getStatus().name()))))

                .andExpect(jsonPath("$[1].id", is(equalTo(entity2.getId()))))
                .andExpect(jsonPath("$[1].name", is(equalTo(entity2.getName()))))
                .andExpect(jsonPath("$[1].description", is(equalTo(entity2.getDescription()))))
                .andExpect(jsonPath("$[1].website", is(equalTo(entity2.getWebsite()))))
                .andExpect(jsonPath("$[1].organisationType", is(equalTo(entity2.getOrganisationType()))))
                .andExpect(jsonPath("$[1].visibility", is(equalTo(entity2.getVisibility().name()))))
                .andExpect(jsonPath("$[1].privacy", is(equalTo(entity2.getPrivacy().name()))))
                .andExpect(jsonPath("$[1].status", is(equalTo(entity2.getStatus().name()))));
    }

    @Test
    public void testGetTeamByVisibilityPrivateWithNoAuthentication() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(null);

        mockMvc.perform(get(TeamsController.PATH + "?visibility=" + TeamVisibility.PRIVATE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetTeamByVisibilityPublicWithNoAuthentication() throws Exception {
        final List<Team> list = new ArrayList<>();
        final TeamEntity entity = getTeamEntityWithId();
        list.add(entity);

        when(securityContext.getAuthentication()).thenReturn(null);
        when(teamService.getTeamsByVisibility(any(TeamVisibility.class))).thenReturn(list);

        mockMvc.perform(get(TeamsController.PATH + "?visibility=" + TeamVisibility.PUBLIC))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity.getId()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity.getDescription()))))
                .andExpect(jsonPath("$[0].website", is(equalTo(entity.getWebsite()))))
                .andExpect(jsonPath("$[0].organisationType", is(equalTo(entity.getOrganisationType()))))
                .andExpect(jsonPath("$[0].visibility", is(equalTo(TeamVisibility.PUBLIC.name()))))
                .andExpect(jsonPath("$[0].privacy", is(equalTo(entity.getPrivacy().name()))))
                .andExpect(jsonPath("$[0].status", is(equalTo(entity.getStatus().name()))));
    }

    @Test
    public void testGetTeamByVisibilityPrivateWithAuthentication() throws Exception {
        final List<Team> list = new ArrayList<>();
        final TeamEntity entity = getTeamEntityWithId();
        entity.setVisibility(TeamVisibility.PRIVATE);
        list.add(entity);

        when(teamService.getTeamsByVisibility(any(TeamVisibility.class))).thenReturn(list);

        mockMvc.perform(get(TeamsController.PATH + "?visibility=" + TeamVisibility.PUBLIC))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity.getId()))))
                .andExpect(jsonPath("$[0].name", is(equalTo(entity.getName()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity.getDescription()))))
                .andExpect(jsonPath("$[0].website", is(equalTo(entity.getWebsite()))))
                .andExpect(jsonPath("$[0].organisationType", is(equalTo(entity.getOrganisationType()))))
                .andExpect(jsonPath("$[0].visibility", is(equalTo(TeamVisibility.PRIVATE.name()))))
                .andExpect(jsonPath("$[0].privacy", is(equalTo(entity.getPrivacy().name()))))
                .andExpect(jsonPath("$[0].status", is(equalTo(entity.getStatus().name()))));
    }

    @Test
    public void testGetTeamByNameTeamNotFoundException() throws Exception {
        String name = RandomStringUtils.randomAlphanumeric(20);

        mockMvc.perform(get(TeamsController.PATH + "?name=" + name))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Team not found"));
    }

    @Test
    public void testGetTeamByName() throws Exception {
        TeamInfo teamInfo = new TeamInfo(getTeamEntityWithId());

        String name = RandomStringUtils.randomAlphanumeric(20);

        when(teamService.getTeamByName(anyString())).thenReturn(teamInfo);

        mockMvc.perform(get(TeamsController.PATH + "?name=" + name))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(equalTo(teamInfo.getId()))))
                .andExpect(jsonPath("$.name", is(equalTo(teamInfo.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(teamInfo.getDescription()))))
                .andExpect(jsonPath("$.website", is(equalTo(teamInfo.getWebsite()))))
                .andExpect(jsonPath("$.organisationType", is(equalTo(teamInfo.getOrganisationType()))))
                .andExpect(jsonPath("$.visibility", is(equalTo(teamInfo.getVisibility().name()))))
                .andExpect(jsonPath("$.privacy", is(equalTo(teamInfo.getPrivacy().name()))))
                .andExpect(jsonPath("$.status", is(equalTo(teamInfo.getStatus().name()))));
    }

    @Test
    public void testGetTeamByIdForbiddenException() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(null);

        mockMvc.perform(get(TeamsController.PATH + "/" + "id"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetTeamByIdTeamNotFoundException() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        mockMvc.perform(get(TeamsController.PATH + "/id"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Team not found"));
    }

    @Test
    public void testGetTeamById() throws Exception {
        TeamInfo teamInfo = new TeamInfo(getTeamEntityWithId());

        String id = RandomStringUtils.randomAlphanumeric(20);

        when(teamService.getTeamById(anyString())).thenReturn(teamInfo);

        mockMvc.perform(get(TeamsController.PATH + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(equalTo(teamInfo.getId()))))
                .andExpect(jsonPath("$.name", is(equalTo(teamInfo.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(teamInfo.getDescription()))))
                .andExpect(jsonPath("$.website", is(equalTo(teamInfo.getWebsite()))))
                .andExpect(jsonPath("$.organisationType", is(equalTo(teamInfo.getOrganisationType()))))
                .andExpect(jsonPath("$.visibility", is(equalTo(teamInfo.getVisibility().name()))))
                .andExpect(jsonPath("$.privacy", is(equalTo(teamInfo.getPrivacy().name()))))
                .andExpect(jsonPath("$.status", is(equalTo(teamInfo.getStatus().name()))));
    }

    @Test
    public void testUpdateTeamForbiddenException() throws Exception {
        final byte[] content = mapper.writeValueAsBytes(new TeamInfo(getTeamEntityWithId()));

        when(securityContext.getAuthentication()).thenReturn(null);

        mockMvc.perform(put(TeamsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateTeam() throws Exception {
        TeamInfo teamInfo = new TeamInfo(getTeamEntityWithId());
        final byte[] content = mapper.writeValueAsBytes(teamInfo);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(teamService.isOwner(anyString(),anyString())).thenReturn(true);
        when(teamService.updateTeam(anyString(), any(Team.class))).thenReturn(teamInfo);

        mockMvc.perform(put(TeamsController.PATH + "/id").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(teamInfo.getId()))))
                .andExpect(jsonPath("$.name", is(equalTo(teamInfo.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(teamInfo.getDescription()))))
                .andExpect(jsonPath("$.website", is(equalTo(teamInfo.getWebsite()))))
                .andExpect(jsonPath("$.organisationType", is(equalTo(teamInfo.getOrganisationType()))))
                .andExpect(jsonPath("$.visibility", is(equalTo(teamInfo.getVisibility().name()))))
                .andExpect(jsonPath("$.privacy", is(equalTo(teamInfo.getPrivacy().name()))))
                .andExpect(jsonPath("$.status", is(equalTo(teamInfo.getStatus().name()))));
    }

    @Test
    public void testGetTeamQuotaByTeamId() throws Exception {
        final String randomUsage = RandomStringUtils.randomNumeric(10);
        TeamQuota teamQuota = getTeamQuotaEntity();
        TeamQuotaInfo teamQuotaInfo = new TeamQuotaInfo(teamQuota,randomUsage);
        final byte[] content = mapper.writeValueAsBytes(teamQuotaInfo);

        TeamEntity team = getTeamEntityWithId();
        when(teamService.getTeamQuotaByTeamId(anyString())).thenReturn(teamQuota);
        when(teamService.getTeamById(anyString())).thenReturn(team);
        when(analyticsService.getUsageStatistics(anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(randomUsage);

        mockMvc.perform(get(TeamsController.PATH + "/teamId/quota").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamId", is(equalTo(teamQuotaInfo.getTeamId()))))
                .andExpect(jsonPath("$.quota", is(equalTo(teamQuotaInfo.getQuota().intValue()))))
                .andExpect(jsonPath("$.usage", is(equalTo(randomUsage))));
    }

    @Test
    public void testUpdateTeamQuotaUnauthorizedException() throws Exception{
        final String randomUsage = RandomStringUtils.randomNumeric(10);
        TeamQuotaInfo teamQuotaInfo = new TeamQuotaInfo(getTeamQuotaEntity(),randomUsage);
        final byte[] content = mapper.writeValueAsBytes(teamQuotaInfo);

        try {
            mockMvc.perform(put(TeamsController.PATH + "/teamId/quota").contentType(MediaType.APPLICATION_JSON).content(content));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(UnauthorizedException.class);
        }
    }


    @Test
    public void testUpdateTeamQuotaForbiddenException() throws Exception {
        final String randomUsage = RandomStringUtils.randomNumeric(10);
        TeamQuotaInfo teamQuotaInfo = new TeamQuotaInfo(getTeamQuotaEntity(),randomUsage);
        final byte[] content = mapper.writeValueAsBytes(teamQuotaInfo);

        when(teamService.isOwner(anyString(),anyString())).thenReturn(false);
        try {
            mockMvc.perform(put(TeamsController.PATH + "/teamId/quota").contentType(MediaType.APPLICATION_JSON).content(content));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(ForbiddenException.class);
        }
    }

    @Test
    public void testUpdateTeamQuotaTeamQuotaOutOfRangeExceptionException1() throws Exception {
        final String randomUsage = RandomStringUtils.randomNumeric(10);
        TeamQuotaEntity teamQuotaEntity = getTeamQuotaEntity();
        teamQuotaEntity.setQuota(BigDecimal.valueOf(0));
        TeamQuotaInfo teamQuotaInfo = new TeamQuotaInfo(teamQuotaEntity,randomUsage);
        final byte[] content = mapper.writeValueAsBytes(teamQuotaInfo);

        TeamEntity team = getTeamEntityWithId();
        when(teamService.isOwner(anyString(),anyString())).thenReturn(true);
        when(teamService.updateTeamQuota(anyString(), any(TeamQuota.class))).thenReturn(teamQuotaEntity);
        when(teamService.getTeamById(anyString())).thenReturn(team);
        when(analyticsService.getUsageStatistics(anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(randomUsage);
        try {
            mockMvc.perform(put(TeamsController.PATH + "/teamId/quota").contentType(MediaType.APPLICATION_JSON).content(content));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(TeamQuotaOutOfRangeException.class);
        }
    }

    @Test
    public void testUpdateTeamQuotaTeamQuotaOutOfRangeException2() throws Exception {
        final String randomUsage = RandomStringUtils.randomNumeric(10);
        TeamQuotaEntity teamQuotaEntity = getTeamQuotaEntity();
        teamQuotaEntity.setQuota(BigDecimal.valueOf(100000000));
        TeamQuotaInfo teamQuotaInfo = new TeamQuotaInfo(getTeamQuotaEntity(),randomUsage);
        final byte[] content = mapper.writeValueAsBytes(teamQuotaInfo);

        TeamEntity team = getTeamEntityWithId();
        when(teamService.updateTeamQuota(anyString(), any(TeamQuota.class))).thenReturn(teamQuotaEntity);
        when(teamService.updateTeamQuota(anyString(), any(TeamQuota.class))).thenReturn(teamQuotaEntity);
        when(analyticsService.getUsageStatistics(anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(randomUsage);
        when(teamService.updateTeamQuota(anyString(), any(TeamQuota.class))).thenReturn(teamQuotaEntity);
        try {
            mockMvc.perform(put(TeamsController.PATH + "/teamId/quota" ).contentType(MediaType.APPLICATION_JSON).content(content));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(TeamQuotaOutOfRangeException.class);
        }
    }

    @Test
    public void testUpdateTeamQuota() throws Exception {
        final String randomUsage = RandomStringUtils.randomNumeric(10);
        final TeamQuotaEntity teamQuotaEntity = getTeamQuotaEntity();
        final TeamQuotaInfo teamQuotaInfo = new TeamQuotaInfo(teamQuotaEntity,randomUsage);
        final byte[] content = mapper.writeValueAsBytes(teamQuotaInfo);

        TeamEntity team = getTeamEntityWithId();
        when(teamService.isOwner(anyString(),anyString())).thenReturn(true);
        when(teamService.getTeamById(anyString())).thenReturn(team);
        when(teamService.updateTeamQuota(anyString(), any(TeamQuota.class))).thenReturn(teamQuotaEntity);
        when(analyticsService.getUsageStatistics(anyString(), any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(randomUsage);
        when(teamService.updateTeamQuota(anyString(), any(TeamQuota.class))).thenReturn(teamQuotaEntity);

        mockMvc.perform(put(TeamsController.PATH + "/teamId/quota").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamId", is(equalTo(teamQuotaInfo.getTeamId()))))
                .andExpect(jsonPath("$.quota", is(equalTo(teamQuotaInfo.getQuota().intValue()))));
    }

    @Test
    public void testUpdateTeamStatusUnauthorizedException() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(null);

        try {
            mockMvc.perform(put(TeamsController.PATH + "/id/status/" + TeamStatus.RESTRICTED));
        } catch (Exception e) {
            assertThat(e.getCause().getClass()).isEqualTo(UnauthorizedException.class);
        }
    }

    @Test
    public void testUpdateTeamStatusNotAdmin() throws Exception {
        final List<String> roles = new ArrayList<>();
        roles.add(Role.USER.getAuthority());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        mockMvc.perform(put(TeamsController.PATH + "/id/status/" + TeamStatus.RESTRICTED))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateTeamStatus() throws Exception {
        TeamEntity entity = getTeamEntityWithId();
        entity.setStatus(TeamStatus.RESTRICTED);
        TeamInfo teamInfo = new TeamInfo(entity);
        final byte[] content = mapper.writeValueAsBytes(teamInfo);

        final List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.getAuthority());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(claims.get(JwtToken.KEY)).thenReturn(roles);
        when(teamService.updateTeamStatus(anyString(), any(TeamStatus.class))).thenReturn(teamInfo);

        mockMvc.perform(put(TeamsController.PATH + "/id/status/" + TeamStatus.RESTRICTED).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(teamInfo.getId()))))
                .andExpect(jsonPath("$.status", is(equalTo(TeamStatus.RESTRICTED.name()))));;
    }

    @Test
    public void testAddTeamMemberForbiddenException() throws Exception {
        final byte[] content = mapper.writeValueAsBytes(getTeamMemberInfo(MemberType.MEMBER, MemberStatus.APPROVED));

        when(securityContext.getAuthentication()).thenReturn(null);

        mockMvc.perform(post(TeamsController.PATH + "/id/members").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAddTeamMember() throws Exception {
        Team team = getTeamEntityWithId();
        final byte[] content = mapper.writeValueAsBytes(getTeamMemberInfo(MemberType.MEMBER, MemberStatus.APPROVED));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(teamService.addMember(anyString(), any(TeamMember.class))).thenReturn(team);

        mockMvc.perform(post(TeamsController.PATH + "/id/members").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(team.getId()))))
                .andExpect(jsonPath("$.name", is(equalTo(team.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(team.getDescription()))))
                .andExpect(jsonPath("$.website", is(equalTo(team.getWebsite()))))
                .andExpect(jsonPath("$.organisationType", is(equalTo(team.getOrganisationType()))))
                .andExpect(jsonPath("$.visibility", is(equalTo(team.getVisibility().name()))))
                .andExpect(jsonPath("$.privacy", is(equalTo(team.getPrivacy().name()))))
                .andExpect(jsonPath("$.status", is(equalTo(team.getStatus().name()))))
                .andExpect(jsonPath("$.members", is(equalTo(team.getMembers()))));
    }

    @Test
    public void testRemoveTeamMemberForbiddenException() throws Exception {
        final byte[] content = mapper.writeValueAsBytes(getTeamMemberInfo(MemberType.MEMBER, MemberStatus.APPROVED));

        when(securityContext.getAuthentication()).thenReturn(null);

        mockMvc.perform(delete(TeamsController.PATH + "/id/members").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRemoveTeamMember() throws Exception {
        Team team = getTeamEntityWithId();
        final byte[] content = mapper.writeValueAsBytes(getTeamMemberInfo(MemberType.MEMBER, MemberStatus.APPROVED));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(claims.getSubject()).thenReturn("ownerId");
        when(teamService.removeMember(anyString(), any(TeamMember.class), anyString())).thenReturn(team);

        mockMvc.perform(delete(TeamsController.PATH + "/id/members").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(team.getId()))))
                .andExpect(jsonPath("$.name", is(equalTo(team.getName()))))
                .andExpect(jsonPath("$.description", is(equalTo(team.getDescription()))))
                .andExpect(jsonPath("$.website", is(equalTo(team.getWebsite()))))
                .andExpect(jsonPath("$.organisationType", is(equalTo(team.getOrganisationType()))))
                .andExpect(jsonPath("$.visibility", is(equalTo(team.getVisibility().name()))))
                .andExpect(jsonPath("$.privacy", is(equalTo(team.getPrivacy().name()))))
                .andExpect(jsonPath("$.status", is(equalTo(team.getStatus().name()))))
                .andExpect(jsonPath("$.members", is(equalTo(team.getMembers()))));
    }

}
