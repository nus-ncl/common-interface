package sg.ncl.service.experiment.logic;

import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.experiment.ExperimentConnectionProperties;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;
import sg.ncl.service.experiment.exceptions.ExperimentNameInUseException;
import sg.ncl.service.experiment.exceptions.UserIdNotFoundException;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Te Ye
 */
public class ExperimentServiceTest2 {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private ExperimentRepository experimentRepository;
    @Mock
    private AdapterDeterLab adapterDeterLab;
    @Mock
    private RealizationService realizationService;
    @Mock
    private ExperimentConnectionProperties experimentConnectionProperties;
    @Mock
    private Claims claims;

    private ExperimentService experimentService;

    @Before
    public void before() {
        assertThat(mockingDetails(experimentRepository).isMock()).isTrue();
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(realizationService).isMock()).isTrue();
        assertThat(mockingDetails(experimentConnectionProperties).isMock()).isTrue();

        experimentService = new ExperimentServiceImpl(experimentRepository, adapterDeterLab, realizationService, experimentConnectionProperties);
    }

    @Test
    public void testSaveExperiment() throws Exception {
        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();

        when(experimentRepository.save(any(ExperimentEntity.class))).thenReturn(createdExperimentSave);

        Experiment savedExperiment = experimentService.save(createdExperimentSave);

        verify(experimentRepository, times(1)).save(any(ExperimentEntity.class));
        assertThat(savedExperiment).isNotNull();
        assertThat(createdExperimentSave.getUserId()).isEqualTo(savedExperiment.getUserId());
        assertThat(createdExperimentSave.getTeamId()).isEqualTo(savedExperiment.getTeamId());
        assertThat(createdExperimentSave.getTeamName()).isEqualTo(savedExperiment.getTeamName());
        assertThat(createdExperimentSave.getName()).isEqualTo(savedExperiment.getName());
        assertThat(createdExperimentSave.getDescription()).isEqualTo(savedExperiment.getDescription());
        assertThat(createdExperimentSave.getNsFile()).isEqualTo(savedExperiment.getNsFile());
        assertThat(createdExperimentSave.getNsFileContent()).isEqualTo(savedExperiment.getNsFileContent());
        assertThat(createdExperimentSave.getIdleSwap()).isEqualTo(savedExperiment.getIdleSwap());
        assertThat(createdExperimentSave.getMaxDuration()).isEqualTo(savedExperiment.getMaxDuration());
    }

    @Test
    public void testSaveExperimentBad() throws Exception {
        // try to create another experiment with same team name and same exp name
        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findByTeamName(anyString())).thenReturn(expList);

        exception.expect(ExperimentNameInUseException.class);

        experimentService.save(createdExperimentSave);
    }

    @Test
    @Ignore
    public void testSaveExperimentDifferentTeamSameExpName() throws Exception {
        ExperimentEntity one = Util.getExperimentsEntity();
        ExperimentEntity two = Util.getExperimentsEntity();
        one.setName("AAA");
        two.setName("BBB");

        when(experimentRepository.save(any(ExperimentEntity.class))).thenAnswer(i -> i.getArgumentAt(0, ExperimentEntity.class));

        Experiment oneSaved = experimentService.save(one);
        Experiment twoSaved = experimentService.save(two);
    }

    @Test
    public void testGetExperimentIfNoExperimentInDb() throws Exception {
        List<Experiment> experimentEntityList = experimentService.getAll();
        assertThat(experimentEntityList).hasSize(0);
    }

    @Test
    public void testGetExperiment() throws Exception {
        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findAll()).thenReturn(expList);

        List<Experiment> result = experimentService.getAll();
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expList);
    }

    @Test
    public void testGetExperimentsByUser() throws Exception {
        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findByUserId(anyString())).thenReturn(expList);

        List<Experiment> result = experimentService.findByUser("userid");
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expList);
    }

    @Test
    public void testGetExperimentsByUserNullId() throws Exception {
        exception.expect(UserIdNotFoundException.class);
        experimentService.findByUser("");
    }

    @Test
    public void testGetExperimentsByTeam() throws Exception {
        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findByTeamId(anyString())).thenReturn(expList);

        List<Experiment> result = experimentService.findByTeam("teamid");
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expList);
    }

    @Test
    public void testGetExperimentsByTeamNullId() throws Exception {
        exception.expect(UserIdNotFoundException.class);
        experimentService.findByTeam("");
    }

    @Test
    public void testGetExperimentsUserHasNoExperiments() throws Exception {
        List<ExperimentEntity> expList = new ArrayList<>();

        when(experimentRepository.findByUserId(anyString())).thenReturn(expList);

        List<Experiment> result = experimentService.findByUser("userid");
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetExperimentsTeamHasNoExperiments() throws Exception {
        List<ExperimentEntity> expList = new ArrayList<>();

        when(experimentRepository.findByTeamId(anyString())).thenReturn(expList);

        List<Experiment> result = experimentService.findByTeam("teamid");
        assertThat(result).isEmpty();
    }

    @Test
    public void testDeleteExperimentGood() throws Exception {
        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        final ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.USER);

        when(experimentRepository.getOne(anyLong())).thenReturn(experimentEntity);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn(realizationEntity.getUserId()); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(1)).delete(anyLong());
        assertThat(experimentEntity).isEqualTo(result);
    }

    @Test
    public void testDeleteExperimentAdmin() throws Exception {
        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        final ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);

        when(experimentRepository.getOne(anyLong())).thenReturn(experimentEntity);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId"); // userid not matched on purpose with realizationEntity because we want to test the role effect
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(1)).delete(anyLong());
        assertThat(experimentEntity).isEqualTo(result);
    }

    @Test
    public void testDeleteExperimentNotExpCreator() throws Exception {
        // to trigger the addCheck() in deleteExperiment
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        final ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.USER);

        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId"); // trigger the add check
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Access denied for delete experiment: expid " + realizationEntity.getExperimentId());
        experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(0)).delete(anyLong());
    }

    @Test
    public void testDeleteExperimentBadAuthoritiesDataType() throws Exception {
        RealizationEntity realizationEntity = Util.getRealizationEntity();

        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId");
        when(claims.get(JwtToken.KEY)).thenReturn("ADMIN"); // not supposed to be a String

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Invalid permissions for delete experiment: expid " + realizationEntity.getExperimentId());
        experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(0)).delete(anyLong());
    }

    @Test
    public void testDeleteExperimentEmptyTeam() throws Exception {
    }

}
