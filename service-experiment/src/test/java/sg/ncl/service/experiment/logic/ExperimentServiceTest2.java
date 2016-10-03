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
    }

    @Test
    public void testGetExperimentsByTeam() throws Exception {
    }

    @Test
    public void testGetExperimentsUserHasNoExperiments() throws Exception {
    }

    @Test
    public void testGetExperimentsTeamHasNoExperiments() throws Exception {
    }

    @Test
    public void testDeleteExperimentGood() throws Exception {
    }

    @Test
    public void testDeleteExperimentAdmin() throws Exception {
    }

    @Test
    public void testDeleteExperimentNotExpCreator() throws Exception {
    }

    @Test
    public void testDeleteExperimentBadAuthoritiesDataType() throws Exception {
    }

    @Test
    public void testDeleteExperimentEmptyTeam() throws Exception {
    }

}
