package sg.ncl.service.experiment.logic;

import freemarker.template.Template;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.common.jwt.JwtToken;
import sg.ncl.service.experiment.data.jpa.ExperimentEntity;
import sg.ncl.service.experiment.data.jpa.ExperimentRepository;
import sg.ncl.service.experiment.domain.Experiment;
import sg.ncl.service.experiment.domain.ExperimentService;
import sg.ncl.service.experiment.exceptions.ExperimentNameAlreadyExistsException;
import sg.ncl.service.experiment.exceptions.TeamIdNullOrEmptyException;
import sg.ncl.service.experiment.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.mail.domain.MailService;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.team.domain.TeamService;
import sg.ncl.service.user.domain.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static sg.ncl.service.experiment.util.TestUtil.getExperimentEntity;
import static sg.ncl.service.experiment.util.TestUtil.getRealizationEntity;

/**
 * @authors: Te Ye, Tran Ly Vu
 */
public class ExperimentServiceTest {

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
    private TeamService teamService;
    @Mock
    private UserService userService;
    @Mock
    private ConnectionProperties adapterConnectionProperties;
    @Mock
    private MailService mailService;
    @Mock
    private Template internetRequestTemplate;
    @Mock
    private Claims claims;

    private ExperimentService experimentService;

    @Before
    public void before() {
        assertThat(mockingDetails(experimentRepository).isMock()).isTrue();
        assertThat(mockingDetails(adapterDeterLab).isMock()).isTrue();
        assertThat(mockingDetails(realizationService).isMock()).isTrue();
        assertThat(mockingDetails(adapterConnectionProperties).isMock()).isTrue();
        assertThat(mockingDetails(teamService).isMock()).isTrue();
        assertThat(mockingDetails(mailService).isMock()).isTrue();
        assertThat(mockingDetails(internetRequestTemplate).isMock()).isTrue();

        experimentService = new ExperimentServiceImpl(experimentRepository, adapterDeterLab, realizationService, adapterConnectionProperties, teamService, userService, mailService, internetRequestTemplate);
    }

    @Test
    public void testSaveExperiment() throws Exception {
        ExperimentEntity createdExperimentSave = getExperimentEntity();

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
        ExperimentEntity createdExperimentSave = getExperimentEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findByTeamName(anyString())).thenReturn(expList);

        exception.expect(ExperimentNameAlreadyExistsException.class);

        experimentService.save(createdExperimentSave);

        verify(experimentRepository, times(0)).save(any(ExperimentEntity.class));
    }

    @Test
    public void testGetExperimentIfNoExperimentInDb() throws Exception {
        List<Experiment> experimentEntityList = experimentService.getAll();
        assertThat(experimentEntityList).hasSize(0);
    }

    @Test
    public void testGetExperiment() throws Exception {
        ExperimentEntity createdExperimentSave = getExperimentEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findAll()).thenReturn(expList);

        List<Experiment> result = experimentService.getAll();
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expList);
    }

    @Test
    public void testGetExperimentByExpId() throws Exception {
        ExperimentEntity entity = getExperimentEntity();

        when(experimentRepository.findOne(anyLong())).thenReturn(entity);

        Experiment one = experimentService.get(1L);

        assertThat(one).isEqualTo(entity);
    }

    @Test
    public void testGetExperimentByExpIdNullId() throws Exception {
        Experiment one = experimentService.get(null);

        assertThat(one).isNull();
    }

    @Test
    public void testGetExperimentsByUser() throws Exception {
        ExperimentEntity createdExperimentSave = getExperimentEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findByUserId(anyString())).thenReturn(expList);

        List<Experiment> result = experimentService.findByUser("userid");
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expList);
    }

    @Test
    public void testGetExperimentsByUserNullId() throws Exception {
        exception.expect(UserIdNullOrEmptyException.class);
        experimentService.findByUser("");
    }

    @Test
    public void testGetExperimentsByTeam() throws Exception {
        ExperimentEntity createdExperimentSave = getExperimentEntity();
        List<ExperimentEntity> expList = new ArrayList<>();
        expList.add(createdExperimentSave);

        when(experimentRepository.findByTeamId(anyString())).thenReturn(expList);

        List<Experiment> result = experimentService.findByTeam("teamid");
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expList);
    }

    @Test
    public void testGetExperimentsByTeamNullId() throws Exception {
        exception.expect(TeamIdNullOrEmptyException.class);
        experimentService.findByTeam("");
    }

    @Test
    public void testGetExperimentDetails() throws Exception {
        String json = RandomStringUtils.randomAlphanumeric(20);
        ExperimentEntity entity = getExperimentEntity();

        when(experimentRepository.getOne(anyLong())).thenReturn(entity);
        when(adapterDeterLab.getExperimentDetails(anyString())).thenReturn(json);

        String result = experimentService.getExperimentDetails("teamId", 1L);
        assertThat(result).isEqualTo(json);
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
        ExperimentEntity experimentEntity = getExperimentEntity();
        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(experimentEntity);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn(realizationEntity.getUserId()); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(1)).delete(anyLong());
        assertThat(experimentEntity).isEqualTo(result);
    }

    @Test
    public void testDeleteExperimentZeroRealizationId() throws Exception {
        ExperimentEntity experimentEntity = getExperimentEntity();
        RealizationEntity realizationEntity = getRealizationEntity();
        realizationEntity.setId(0L); // set zero to trigger another branch

        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(experimentEntity);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn(realizationEntity.getUserId()); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(0)).delete(anyLong());
        assertThat(result).isNull();
    }

    @Test
    public void testDeleteExperimentTeamOwner() throws Exception {
        ExperimentEntity experimentEntity = getExperimentEntity();
        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(experimentEntity);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(claims.getSubject()).thenReturn("ownerId"); // claims user id should be the team owner
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(1)).delete(anyLong());
        assertThat(experimentEntity).isEqualTo(result);
    }

    @Test
    public void testDeleteExperimentAdmin() throws Exception {
        ExperimentEntity experimentEntity = getExperimentEntity();
        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.ADMIN.getAuthority());

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
        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId"); // trigger the add check
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Permission denied");
        experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(0)).delete(anyLong());
    }

    @Test
    public void testDeleteExperimentBadAuthoritiesDataType() throws Exception {
        RealizationEntity realizationEntity = getRealizationEntity();

        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId");
        when(claims.get(JwtToken.KEY)).thenReturn("ADMIN"); // not supposed to be a String

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Permission denied");
        experimentService.deleteExperiment(1L, "teamName", claims);

        verify(experimentRepository, times(0)).delete(anyLong());
    }

    @Test
    public void testUpdateExperimentGood() throws Exception {
        ExperimentEntity original = getExperimentEntity();
        ExperimentEntity edited = getExperimentEntity();

        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(original);
        when(experimentRepository.save(any(ExperimentEntity.class))).thenReturn(edited);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn(realizationEntity.getUserId()); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.updateExperiment(1L, "teamName", edited, claims);

        assertThat(result).isEqualTo(edited);
    }

    @Test
    public void testUpdateExperimentTeamOwner() throws Exception {
        ExperimentEntity original = getExperimentEntity();
        ExperimentEntity edited = getExperimentEntity();

        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(original);
        when(experimentRepository.save(any(ExperimentEntity.class))).thenReturn(edited);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(teamService.isOwner(anyString(), anyString())).thenReturn(true);
        when(claims.getSubject()).thenReturn("ownerId"); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.updateExperiment(1L, "teamName", edited, claims);

        assertThat(result).isEqualTo(edited);
    }

    @Test
    public void testUpdateExperimentAdmin() throws Exception {
        ExperimentEntity original = getExperimentEntity();
        ExperimentEntity edited = getExperimentEntity();

        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.ADMIN.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(original);
        when(experimentRepository.save(any(ExperimentEntity.class))).thenReturn(edited);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId"); // userid not matched on purpose with realizationEntity because we want to test the role effect
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        Experiment result = experimentService.updateExperiment(1L, "teamName", edited, claims);

        assertThat(result).isEqualTo(edited);
    }

    @Test
    public void testUpdateExperimentNotExpCreator() throws Exception {
        ExperimentEntity original = getExperimentEntity();
        ExperimentEntity edited = getExperimentEntity();

        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(original);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId"); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn(roles);

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Permission denied");
        experimentService.updateExperiment(1L, "teamName", edited, claims);
    }

    @Test
    public void testUpdateExperimentBadAuthoritiesDataType() throws Exception {
        ExperimentEntity original = getExperimentEntity();
        ExperimentEntity edited = getExperimentEntity();

        RealizationEntity realizationEntity = getRealizationEntity();
        final List<String> roles = Collections.singletonList(Role.USER.getAuthority());

        when(experimentRepository.getOne(anyLong())).thenReturn(original);
        when(realizationService.getByExperimentId(anyLong())).thenReturn(realizationEntity);
        when(claims.getSubject()).thenReturn("userId"); // claims user id should be identical to realizationEntity user id
        when(claims.get(JwtToken.KEY)).thenReturn("ADMIN"); // not supposed to be a String

        exception.expect(ForbiddenException.class);
        exception.expectMessage("Permission denied");
        experimentService.updateExperiment(1L, "teamName", edited, claims);
    }
}
