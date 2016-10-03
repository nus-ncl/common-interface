//package sg.ncl.service.experiment.logic;
//
//import io.jsonwebtoken.Claims;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.Assert;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//import org.springframework.test.context.ActiveProfiles;
//import sg.ncl.common.authentication.Role;
//import sg.ncl.common.exception.base.ForbiddenException;
//import sg.ncl.common.jwt.JwtToken;
//import sg.ncl.service.experiment.AbstractTest;
//import sg.ncl.service.experiment.Util;
//import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
//import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
//import sg.ncl.service.experiment.domain.Experiment;
//import sg.ncl.service.experiment.domain.ExperimentService;
//import sg.ncl.service.experiment.exceptions.ExperimentNameInUseException;
//import sg.ncl.service.realization.data.jpa.RealizationEntity;
//import sg.ncl.service.realization.data.jpa.RealizationRepository;
//
//import javax.inject.Inject;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.not;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * Created by Desmond.
// */
//@ActiveProfiles({"mock-deter-adapter"})
//public class ExperimentServiceTest extends AbstractTest {
//
//    @Rule
//    public MockitoRule mockito = MockitoJUnit.rule();
//
//    @Rule
//    public final ExpectedException exception = ExpectedException.none();
//
//    @Inject
//    private ExperimentRepository experimentRepository;
//    @Inject
//    private ExperimentService experimentService;
//    @Inject
//    private RealizationRepository realizationRepository;
//
//    @Test
//    public void testSaveExperiment() throws Exception {
//
//        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();
//
//        Experiment savedExperiment = experimentService.save(createdExperimentSave);
//
//        Assert.assertNotNull(savedExperiment);
//        Assert.assertEquals(createdExperimentSave.getUserId(), savedExperiment.getUserId());
//        Assert.assertEquals(createdExperimentSave.getTeamId(), savedExperiment.getTeamId());
//        Assert.assertEquals(createdExperimentSave.getTeamName(), savedExperiment.getTeamName());
//        Assert.assertEquals(createdExperimentSave.getName(), savedExperiment.getName());
//        Assert.assertEquals(createdExperimentSave.getDescription(), savedExperiment.getDescription());
//        Assert.assertEquals(createdExperimentSave.getIdleSwap(), savedExperiment.getIdleSwap());
//        Assert.assertEquals(createdExperimentSave.getMaxDuration(), savedExperiment.getMaxDuration());
//
//        // check new nsFile name
//        String craftDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
//        String filename = createdExperimentSave.getUserId() + "_" + createdExperimentSave.getTeamId() + "_" + craftDate + "_" + createdExperimentSave.getNsFile() + ".ns";
//        Assert.assertEquals(filename, savedExperiment.getNsFile());
//    }
//
//    @Test(expected = ExperimentNameInUseException.class)
//    public void testSaveExperimentBad() throws Exception {
//        // try to create another experiment with same team name and same exp name
//        ExperimentEntity createdExperimentSave = Util.getExperimentsEntity();
//        Experiment savedExperiment = experimentService.save(createdExperimentSave);
//        experimentService.save(createdExperimentSave);
//    }
//
//    @Test
//    public void testSaveExperimentDifferentTeamSameExpName() throws Exception {
//        ExperimentEntity one = Util.getExperimentsEntity();
//        ExperimentEntity two = Util.getExperimentsEntity();
//        final String expName = RandomStringUtils.randomAlphabetic(8);
//        one.setName(expName);
//        two.setName(expName);
//        Experiment oneSaved = experimentService.save(one);
//        Experiment twoSaved = experimentService.save(two);
//
//        Assert.assertThat(oneSaved.getName(), is(equalTo(twoSaved.getName())));
//        Assert.assertThat(oneSaved.getTeamName(), is(not(equalTo(twoSaved.getTeamName()))));
//    }
//
//    @Test
//    public void testGetExperimentIfNoExperimentInDb() throws Exception {
//
//        List<Experiment> experimentEntityList = experimentService.getAll();
//
//        Assert.assertEquals(experimentEntityList.size(), 0);
//    }
//
//    @Test
//    public void testGetExperiment() throws Exception {
//        List<Experiment> list = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            list.add(experimentRepository.save(Util.getExperimentsEntity()));
//        }
//
//        List<Experiment> listFromDb = experimentService.getAll();
//        Assert.assertTrue(Util.isListEqual(list, listFromDb));
//    }
//
//    @Test
//    public void testGetExperimentsByUser() throws Exception {
//
//        int numEntries = 6;
//
//        final String userId = RandomStringUtils.randomAlphanumeric(20);
//        Util.addExperimentsChangeUserId(numEntries, userId, experimentRepository);
//
//        List<Experiment> list = experimentService.findByUser(userId);
//        Assert.assertEquals(list.size(), numEntries / 2);
//
//        for (Experiment forEntity : list) {
//            Assert.assertEquals(forEntity.getUserId(), userId);
//        }
//
//        List<Experiment> allExperiments = experimentService.getAll();
//        Assert.assertEquals(allExperiments.size(), numEntries);
//    }
//
//    @Test
//    public void testGetExperimentsByTeam() throws Exception {
//
//        int numEntries = 6;
//
//        final String teamId = RandomStringUtils.randomAlphanumeric(20);
//        Util.addExperimentsChangeTeamId(numEntries, teamId, experimentRepository);
//
//        List<Experiment> list = experimentService.findByTeam(teamId);
//        Assert.assertEquals(list.size(), numEntries / 2);
//
//        for (Experiment forEntity : list) {
//            Assert.assertEquals(forEntity.getTeamId(), teamId);
//        }
//
//        List<Experiment> allExperiments = experimentService.getAll();
//        Assert.assertEquals(allExperiments.size(), numEntries);
//    }
//
//    @Test
//    public void testGetExperimentsUserHasNoExperiments() throws Exception {
//        final String userId = RandomStringUtils.randomAlphanumeric(20);
//        for (int i = 0; i < 3; i++) {
//            experimentRepository.save(Util.getExperimentsEntity());
//        }
//
//        List<Experiment> list = experimentService.findByUser(userId);
//        Assert.assertEquals(list.size(), 0);
//    }
//
//    @Test
//    public void testGetExperimentsTeamHasNoExperiments() throws Exception {
//        final String teamId = RandomStringUtils.randomAlphanumeric(20);
//        for (int i = 0; i < 3; i++) {
//            experimentRepository.save(Util.getExperimentsEntity());
//        }
//
//        List<Experiment> list = experimentService.findByTeam(teamId);
//        Assert.assertEquals(list.size(), 0);
//    }
//
//    @Test
//    public void testDeleteExperimentGood() throws Exception {
//        final Claims claims = mock(Claims.class);
//        final ArrayList<Role> roles = new ArrayList<>();
//        roles.add(Role.USER);
//
//        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
//        ExperimentEntity savedExperiment = experimentRepository.save(experimentEntity);
//
//        RealizationEntity realizationEntity = new RealizationEntity();
//        realizationEntity.setExperimentId(savedExperiment.getId());
//        realizationEntity.setExperimentName(savedExperiment.getName());
//        realizationEntity.setUserId(savedExperiment.getUserId());
//        realizationEntity.setTeamId(savedExperiment.getTeamId());
//        realizationEntity.setNumberOfNodes(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setIdleMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setRunningMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationRepository.save(realizationEntity);
//
//        compareCounts(1L);
//
//        when(claims.getSubject()).thenReturn(savedExperiment.getUserId());
//        when(claims.get(JwtToken.KEY)).thenReturn(roles);
//
//
//        experimentService.deleteExperiment(savedExperiment.getId(), experimentEntity.getTeamName(), claims);
//
//        compareCounts(0L);
//    }
//
//    @Test
//    public void testDeleteExperimentAdmin() throws Exception {
//        String notOwnerId = RandomStringUtils.randomAlphanumeric(20);
//        final Claims claims = mock(Claims.class);
//        final ArrayList<Role> roles = new ArrayList<>();
//        roles.add(Role.ADMIN);
//
//        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
//        ExperimentEntity savedExperiment = experimentRepository.save(experimentEntity);
//
//        RealizationEntity realizationEntity = new RealizationEntity();
//        realizationEntity.setExperimentId(savedExperiment.getId());
//        realizationEntity.setExperimentName(savedExperiment.getName());
//        realizationEntity.setUserId(savedExperiment.getUserId());
//        realizationEntity.setTeamId(savedExperiment.getTeamId());
//        realizationEntity.setNumberOfNodes(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setIdleMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setRunningMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationRepository.save(realizationEntity);
//
//        when(claims.getSubject()).thenReturn(notOwnerId);
//        when(claims.get(JwtToken.KEY)).thenReturn(roles);
//
//        experimentService.deleteExperiment(savedExperiment.getId(), experimentEntity.getTeamName(), claims);
//    }
//
//    @Test
//    public void testDeleteExperimentNotExpCreator() throws Exception {
//        String notOwnerId = RandomStringUtils.randomAlphanumeric(20);
//        final Claims claims = mock(Claims.class);
//        final ArrayList<Role> roles = new ArrayList<>();
//        roles.add(Role.USER);
//
//        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
//        ExperimentEntity savedExperiment = experimentRepository.save(experimentEntity);
//
//        RealizationEntity realizationEntity = new RealizationEntity();
//        realizationEntity.setExperimentId(savedExperiment.getId());
//        realizationEntity.setExperimentName(savedExperiment.getName());
//        realizationEntity.setUserId(savedExperiment.getUserId());
//        realizationEntity.setTeamId(savedExperiment.getTeamId());
//        realizationEntity.setNumberOfNodes(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setIdleMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setRunningMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationRepository.save(realizationEntity);
//
//        when(claims.getSubject()).thenReturn(notOwnerId);
//        when(claims.get(JwtToken.KEY)).thenReturn(roles);
//
//        exception.expect(ForbiddenException.class);
//        exception.expectMessage(is(equalTo("Access denied for delete experiment: expid " + savedExperiment.getId())));
//        experimentService.deleteExperiment(savedExperiment.getId(), experimentEntity.getTeamId(), claims);
//    }
//
//    @Test
//    public void testDeleteExperimentBadAuthoritiesDataType() throws Exception {
//        final Claims claims = mock(Claims.class);
//
//        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
//        ExperimentEntity savedExperiment = experimentRepository.save(experimentEntity);
//
//        RealizationEntity realizationEntity = new RealizationEntity();
//        realizationEntity.setExperimentId(savedExperiment.getId());
//        realizationEntity.setExperimentName(savedExperiment.getName());
//        realizationEntity.setUserId(savedExperiment.getUserId());
//        realizationEntity.setTeamId(savedExperiment.getTeamId());
//        realizationEntity.setNumberOfNodes(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setIdleMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setRunningMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationRepository.save(realizationEntity);
//
//        when(claims.getSubject()).thenReturn(savedExperiment.getUserId());
//        when(claims.get(JwtToken.KEY)).thenReturn("ADMIN"); // not supposed to be a String
//
//        exception.expect(ForbiddenException.class);
//        exception.expectMessage(is(equalTo("Invalid permissions for delete experiment: expid " + savedExperiment.getId())));
//        experimentService.deleteExperiment(savedExperiment.getId(), experimentEntity.getTeamId(), claims);
//    }
//
//    @Test
//    public void testDeleteExperimentEmptyTeam() throws Exception {
//        final Claims claims = mock(Claims.class);
//        final ArrayList<Role> roles = new ArrayList<>();
//        roles.add(Role.USER);
//
//        ExperimentEntity experimentEntity = Util.getExperimentsEntity();
//        experimentEntity.setTeamName("");
//        ExperimentEntity savedExperiment = experimentRepository.save(experimentEntity);
//
//        RealizationEntity realizationEntity = new RealizationEntity();
//        realizationEntity.setExperimentId(savedExperiment.getId());
//        realizationEntity.setExperimentName(savedExperiment.getName());
//        realizationEntity.setUserId(savedExperiment.getUserId());
//        realizationEntity.setTeamId(savedExperiment.getTeamId());
//        realizationEntity.setNumberOfNodes(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setIdleMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationEntity.setRunningMinutes(Long.parseLong(RandomStringUtils.randomNumeric(5)));
//        realizationRepository.save(realizationEntity);
//
//        when(claims.getSubject()).thenReturn(savedExperiment.getUserId());
//        when(claims.get(JwtToken.KEY)).thenReturn(roles);
//
//        exception.expect(ForbiddenException.class);
//        exception.expectMessage(is(equalTo("Error locating team name using team id " + experimentEntity.getTeamId())));
//        experimentService.deleteExperiment(savedExperiment.getId(), experimentEntity.getTeamId(), claims);
//    }
//
//    private void compareCounts(Long count) {
//        Long experimentCount = experimentRepository.count();
//        Long realizationCount = realizationRepository.count();
//
//        Assert.assertEquals(experimentCount, count);
//        Assert.assertEquals(realizationCount, count);
//    }
//}
