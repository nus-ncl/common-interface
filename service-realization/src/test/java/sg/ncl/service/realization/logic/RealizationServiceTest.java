package sg.ncl.service.realization.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.service.realization.AbstractTest;
import sg.ncl.service.realization.Util;
import sg.ncl.service.realization.data.jpa.RealizationEntity;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.domain.RealizationState;

import javax.inject.Inject;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by Desmond.
 */
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
    private AdapterDeterlab adapterDeterlab;

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

        Assert.assertEquals(savedRealizationEntity.getState(), RealizationState.NEW);
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
        predefinedResultJson.put("msg", "Experiment started");
        predefinedResultJson.put("status", "active");
        predefinedResultJson.put("report", "this is a report");

        mockServer.expect(requestTo(properties.startExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String teamName = RandomStringUtils.randomAlphanumeric(20);
        String userId = RandomStringUtils.randomAlphanumeric(20);

        RealizationEntity one = Util.getRealizationEntity();
        String experimentName = one.getExperimentName();
        realizationService.save(one);

        adapterDeterlab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(20), userId);
        RealizationEntity result = realizationService.startExperimentInDeter(teamName, experimentName, userId);

        Assert.assertNotEquals(one.getState(), result.getState());
        Assert.assertNotEquals(one.getDetails(), result.getDetails());
    }

    @Test
    public void testStopExperiment() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "Experiment stopped");

        mockServer.expect(requestTo(properties.stopExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String teamName = RandomStringUtils.randomAlphanumeric(20);
        String experimentName = RandomStringUtils.randomAlphanumeric(20);
        String userId = RandomStringUtils.randomAlphanumeric(20);

        adapterDeterlab.saveDeterUserIdMapping(RandomStringUtils.randomAlphanumeric(20), userId);
        String httpCommand = realizationService.stopExperimentInDeter(teamName, experimentName, userId);

        String returnString = "{\"msg\":\"Experiment stopped\"}";

        Assert.assertEquals(httpCommand, returnString);
    }
}
