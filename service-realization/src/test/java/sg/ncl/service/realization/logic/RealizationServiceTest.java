package sg.ncl.service.realization.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.service.realization.AbstractTest;
import sg.ncl.service.realization.Util;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationService;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by Desmond.
 */
@TestPropertySource(properties = "flyway.enabled=false")
public class RealizationServiceTest extends AbstractTest {

    @Inject
    private RealizationService realizationService;

    @Inject
    private RealizationRepository realizationRepository;

    @Inject
    private ConnectionProperties properties;

    @Inject
    private RestOperations restOperations;

    private MockRestServiceServer mockServer;

    @Inject
    private AdapterDeterLab adapterDeterLab;

    @Before
    public void setUp() throws Exception {
        mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test
    public void testGetRealizationWithWrongId() {

        realizationRepository.save((Util.getRealizationEntity()));

        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        RealizationEntity realizationEntityDb = realizationService.getById(id);

        Assert.assertNull(realizationEntityDb);
    }

    @Test
    public void testGetRealizationWithId() {

        RealizationEntity savedRealizationEntity = realizationRepository.save((Util.getRealizationEntity()));

        Long id = savedRealizationEntity.getId();
        RealizationEntity realizationEntityDb = realizationService.getById(id);

        Assert.assertNotNull(realizationEntityDb);
    }

    @Test
    public void testRealizationWithWrongExperimentId() {

        realizationRepository.save((Util.getRealizationEntity()));

        Long experimentId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(experimentId);

        Assert.assertNull(realizationEntityDb);
    }

    @Test
    public void testGetRealizationWithExperimentId() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        Long experimentId = realizationEntity.getExperimentId();

        realizationRepository.save((realizationEntity));
        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(experimentId);

        Assert.assertNotNull(realizationEntityDb);
    }

    @Test
    public void testGetRealizationWithTeamAndExperimentId() {
        String teamName = RandomStringUtils.randomAlphabetic(8);
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        realizationEntity.setState(RealizationState.NOT_RUNNING);
        Long experimentId = realizationEntity.getExperimentId();

        realizationRepository.save((realizationEntity));

        JSONObject reportJson = new JSONObject();
        reportJson.put("test", "test2");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "active");
        predefinedResultJson.put("report", reportJson);

        JSONObject input = new JSONObject();
        input.put("pid", teamName);
        input.put("eid", realizationEntity.getExperimentId());

        mockServer.expect(requestTo(properties.getExpStatus()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        RealizationEntity realizationEntityDb = realizationService.getByExperimentId(teamName, experimentId);

        Assert.assertNotNull(realizationEntityDb);
        Assert.assertThat(realizationEntityDb.getState(), is(RealizationState.RUNNING));
        Assert.assertThat(realizationEntityDb.getDetails(), is("{\"test\":\"test2\"}"));
    }

    @Test
    public void testSaveRealization() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntiy = realizationService.save(realizationEntity);

        Assert.assertEquals(realizationEntity.getUserId(), savedRealizationEntiy.getUserId());
        Assert.assertEquals(realizationEntity.getTeamId(), savedRealizationEntiy.getTeamId());
        Assert.assertEquals(realizationEntity.getExperimentId(), savedRealizationEntiy.getExperimentId());
        Assert.assertEquals(realizationEntity.getNumberOfNodes(), savedRealizationEntiy.getNumberOfNodes());
        Assert.assertEquals(realizationEntity.getState(), savedRealizationEntiy.getState());
        Assert.assertEquals(realizationEntity.getIdleMinutes(), savedRealizationEntiy.getIdleMinutes());
        Assert.assertEquals(realizationEntity.getRunningMinutes(), savedRealizationEntiy.getRunningMinutes());
    }

    @Test
    public void testGetState() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getState(), RealizationState.NOT_RUNNING);
    }

    @Test
    public void testSetState() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();

        realizationService.setState(experimentId, RealizationState.WARNING);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getState(), RealizationState.WARNING);
    }

    @Test
    public void testGetIdleMinutes() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getIdleMinutes(), realizationEntity.getIdleMinutes());
    }

    @Test
    public void testSetIdleMinutes() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();
        Long minutes = Long.parseLong(RandomStringUtils.randomNumeric(5));

        realizationService.setIdleMinutes(experimentId, minutes);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getIdleMinutes(), minutes);
    }

    @Test
    public void testGetRunningMinutes() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getRunningMinutes(), realizationEntity.getRunningMinutes());
    }

    @Test
    public void testSetRunningMinutes() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();
        Long minutes = Long.parseLong(RandomStringUtils.randomNumeric(5));

        realizationService.setRunningMinutes(experimentId, minutes);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getRunningMinutes(), minutes);
    }

    @Test
    public void testGetRealizationDetails() {
        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Assert.assertEquals(savedRealizationEntity.getDetails(), realizationEntity.getDetails());
    }

    @Test
    public void testSetRealizationDetails() {

        RealizationEntity realizationEntity = Util.getRealizationEntity();
        RealizationEntity savedRealizationEntity = realizationRepository.save(realizationEntity);

        Long experimentId = savedRealizationEntity.getExperimentId();
        String details = RandomStringUtils.randomAlphanumeric(20);

        realizationService.setRealizationDetails(experimentId, details);

        RealizationEntity realizationEntityDB = realizationRepository.findByExperimentId(experimentId);

        Assert.assertEquals(realizationEntityDB.getDetails(), details);
    }

    @Test
    public void testStartExperiment() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment start success");
        predefinedResultJson.put("status", "active");
        predefinedResultJson.put("report", new JSONObject());

        mockServer.expect(requestTo(properties.startExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        RealizationEntity one = Util.getRealizationEntity();
        realizationService.save(one);

        String teamName = RandomStringUtils.randomAlphanumeric(20);
        String userId = one.getUserId();
        String expId = one.getExperimentId().toString();

        adapterDeterLab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(20), userId);
        RealizationEntity result = realizationService.startExperimentInDeter(teamName, expId);

        Assert.assertNotEquals(one.getState(), result.getState());
        Assert.assertNotEquals(one.getDetails(), result.getDetails());
    }

    @Test
    public void testStopExperimentNotRunning() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "swapped");

        mockServer.expect(requestTo(properties.stopExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        RealizationEntity one = Util.getRealizationEntity();
        realizationService.save(one);

        String teamName = RandomStringUtils.randomAlphanumeric(20);
        String userId = one.getUserId();
        String expId = one.getExperimentId().toString();

        adapterDeterLab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(20), userId);
        RealizationEntity result = realizationService.stopExperimentInDeter(teamName, expId);

        Assert.assertThat(result.getState(), is(RealizationState.NOT_RUNNING));
        Assert.assertThat(result.getDetails(), is(""));
    }

    @Test
    public void testStopExperimentError() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "error");

        mockServer.expect(requestTo(properties.stopExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        RealizationEntity one = Util.getRealizationEntity();
        realizationService.save(one);

        String teamName = RandomStringUtils.randomAlphanumeric(20);
        String userId = one.getUserId();
        String expId = one.getExperimentId().toString();

        adapterDeterLab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(20), userId);
        RealizationEntity result = realizationService.stopExperimentInDeter(teamName, expId);

        Assert.assertThat(result.getState(), is(RealizationState.ERROR));
        Assert.assertThat(result.getDetails(), is(""));
    }
}
