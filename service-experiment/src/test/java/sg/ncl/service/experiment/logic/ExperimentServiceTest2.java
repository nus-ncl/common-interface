package sg.ncl.service.experiment.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.service.experiment.ExperimentConnectionProperties;
import sg.ncl.service.experiment.Util;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;
import sg.ncl.service.experiment.web.ExperimentInfo;
import sg.ncl.service.realization.domain.RealizationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        Assert.assertNotNull(savedExperiment);
        Assert.assertEquals(createdExperimentSave.getUserId(), savedExperiment.getUserId());
        Assert.assertEquals(createdExperimentSave.getTeamId(), savedExperiment.getTeamId());
        Assert.assertEquals(createdExperimentSave.getTeamName(), savedExperiment.getTeamName());
        Assert.assertEquals(createdExperimentSave.getName(), savedExperiment.getName());
        Assert.assertEquals(createdExperimentSave.getDescription(), savedExperiment.getDescription());
        Assert.assertEquals(createdExperimentSave.getNsFile(), savedExperiment.getNsFile());
        Assert.assertEquals(createdExperimentSave.getNsFileContent(), savedExperiment.getNsFileContent());
        Assert.assertEquals(createdExperimentSave.getIdleSwap(), savedExperiment.getIdleSwap());
        Assert.assertEquals(createdExperimentSave.getMaxDuration(), savedExperiment.getMaxDuration());
    }

    @Test
    public void testSaveExperimentBad() throws Exception {

    }

    @Test
    public void testSaveExperimentDifferentTeamSameExpName() throws Exception {

    }

    @Test
    public void testGetExperimentIfNoExperimentInDb() throws Exception {

    }

    @Test
    public void testGetExperiment() throws Exception {

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
