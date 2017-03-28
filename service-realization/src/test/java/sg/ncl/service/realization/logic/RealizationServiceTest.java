package sg.ncl.service.realization.logic;

import io.jsonwebtoken.Claims;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.TestPropertySource;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.Realization;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.realization.domain.RealizationState;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamQuota;
import sg.ncl.service.team.domain.TeamService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static sg.ncl.service.realization.util.TestUtil.getRealizationEntity;
import static sg.ncl.service.realization.util.TestUtil.getTeamEntityWithId;
import static sg.ncl.service.realization.util.TestUtil.getTeamQuotaEntity;

/**
 * Created by Desmond.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class RealizationServiceTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private RealizationRepository realizationRepository;
    @Mock
    private AdapterDeterLab adapterDeterLab;
    @Mock
    private TeamService teamService;
    @Mock
    private Claims claims;

    private RealizationService realizationService;

    @Before
    public void before() throws Exception {
        assertThat(mockingDetails(realizationRepository).isMock()).isTrue();
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(teamService).isMock()).isTrue();
        assertThat(mockingDetails(claims).isMock()).isTrue();

        realizationService = new RealizationServiceImpl(realizationRepository, adapterDeterLab, teamService);
    }

    @Test
    public void testGetAllRealizations() {
        RealizationEntity entity1 = getRealizationEntity();
        RealizationEntity entity2 = getRealizationEntity();
        RealizationEntity entity3 = getRealizationEntity();

        List<RealizationEntity> realizationList = new ArrayList<>();
        realizationList.add(entity1);
        realizationList.add(entity2);
        realizationList.add(entity3);

        when(realizationRepository.findAll()).thenReturn(realizationList);

        List<Realization> result = realizationService.getAll();

        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(realizationList);
    }

    @Test
    public void testGetRealizationWithWrongId() {
        when(realizationRepository.findOne(anyLong())).thenReturn(null);

        Realization result = realizationService.getById(1L);

        assertThat(result).isNull();
    }

    @Test
    public void testGetRealizationWithId() {
        RealizationEntity entity = getRealizationEntity();

        when(realizationRepository.findOne(anyLong())).thenReturn(entity);

        Realization result = realizationService.getById(entity.getId());

        assertThat(result.getId()).isEqualTo(entity.getId());
    }

    @Test
    public void testRealizationWithWrongExperimentId() {
        when(realizationRepository.findOne(anyLong())).thenReturn(null);

        Realization result = realizationService.getByExperimentId(1L);

        assertThat(result).isNull();
    }

    @Test
    public void testGetRealizationWithExperimentId() {
        RealizationEntity entity = getRealizationEntity();

        when(realizationRepository.findByExperimentId(entity.getExperimentId())).thenReturn(entity);

        Realization result = realizationService.getByExperimentId(entity.getExperimentId());

        assertThat(result.getExperimentId()).isEqualTo(entity.getExperimentId());
    }

    @Test
    public void testGetRealizationWithTeamAndExperimentId() {
        RealizationEntity entity = getRealizationEntity();

        JSONObject predefinedResultJson = new JSONObject();
        JSONObject reportJson = new JSONObject();
        reportJson.put("test", "test2");
        predefinedResultJson.put("status", "active");
        predefinedResultJson.put("report", reportJson);

        when(realizationRepository.findByExperimentId(anyLong())).thenReturn(entity);
        when(realizationRepository.save(any(RealizationEntity.class))).thenReturn(entity);
        when(adapterDeterLab.getExperimentStatus(anyString(), anyString())).thenReturn(predefinedResultJson.toString());

        Realization result = realizationService.getByExperimentId("teamName", 1L);

        assertThat(result.getState()).isEqualTo(RealizationState.RUNNING);
        assertThat(result.getDetails()).isEqualTo("{\"test\":\"test2\"}");
    }

    @Test
    public void testSaveRealization() {
        RealizationEntity entity = getRealizationEntity();

        when(realizationRepository.save(any(RealizationEntity.class))).thenReturn(entity);

        Realization result = realizationService.save(entity);

        assertThat(result.getUserId()).isEqualTo(entity.getUserId());
        assertThat(result.getTeamId()).isEqualTo(entity.getTeamId());
        assertThat(result.getExperimentId()).isEqualTo(entity.getExperimentId());
        assertThat(result.getNumberOfNodes()).isEqualTo(entity.getNumberOfNodes());
        assertThat(result.getState()).isEqualTo(entity.getState());
        assertThat(result.getRunningMinutes()).isEqualTo(entity.getRunningMinutes());
    }

    @Test
    public void testStartExperiment() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment start success");
        predefinedResultJson.put("status", "active");
        predefinedResultJson.put("report", "");
        RealizationEntity entity = getRealizationEntity();
        Team team =  getTeamEntityWithId();
        TeamQuota teamQuota = getTeamQuotaEntity();

        when(teamService.getTeamByName(anyString())).thenReturn(team);
        when(teamService.getTeamQuotaByTeamId(anyString())).thenReturn(teamQuota);
        when(realizationRepository.findByExperimentId(anyLong())).thenReturn(entity);
        when(realizationRepository.save(any(RealizationEntity.class))).thenReturn(entity);
        when(adapterDeterLab.startExperiment(anyString(), anyString(), anyString())).thenReturn(predefinedResultJson.toString());

        Realization result = realizationService.startExperimentInDeter("teamName", "1", claims);

        assertThat(result.getState()).isEqualTo(RealizationState.RUNNING);
    }

    @Test
    public void testStopExperimentNotRunning() {
        RealizationEntity entity = getRealizationEntity();

        when(realizationRepository.findByExperimentId(anyLong())).thenReturn(entity);
        when(realizationRepository.save(any(RealizationEntity.class))).thenReturn(entity);
        when(adapterDeterLab.stopExperiment(anyString(), anyString(), anyString())).thenReturn("swapped");

        Realization result = realizationService.stopExperimentInDeter("teamName", "1", claims);

        assertThat(result.getState()).isEqualTo(RealizationState.NOT_RUNNING);
    }

    @Test
    public void testStopExperimentError() {
        RealizationEntity entity = getRealizationEntity();

        when(realizationRepository.findByExperimentId(anyLong())).thenReturn(entity);
        when(realizationRepository.save(any(RealizationEntity.class))).thenReturn(entity);
        when(adapterDeterLab.stopExperiment(anyString(), anyString(), anyString())).thenReturn("error");

        Realization result = realizationService.stopExperimentInDeter("teamName", "1", claims);

        assertThat(result.getState()).isEqualTo(RealizationState.ERROR);
    }
}
