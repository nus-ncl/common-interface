package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.AdapterDeterlabConnectException;
import sg.ncl.adapter.deterlab.exceptions.CredentialsUpdateException;
import sg.ncl.adapter.deterlab.exceptions.DeterLabOperationFailedException;
import sg.ncl.adapter.deterlab.exceptions.ExpDeleteException;
import sg.ncl.adapter.deterlab.exceptions.ExpNameAlreadyExistsException;
import sg.ncl.adapter.deterlab.exceptions.ExpStartException;
import sg.ncl.adapter.deterlab.exceptions.NSFileParseException;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Te Ye
 */
public class AdapterDeterLabTest extends AbstractTest {

    @Autowired
    private RestOperations restOperations;

    @Inject
    private DeterLabUserRepository deterLabUserRepository;

    @Inject
    private AdapterDeterLab adapterDeterLab;

    @Inject
    private ConnectionProperties properties;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test
    public void testJoinProjectNewUsersGood() {
        JSONObject userObject = Util.getUserAdapterJSONObject();

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getJoinProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.joinProjectNewUsers(userObject.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        String uid = resultJSONObject.getString("uid");
        Assert.assertThat(msg, is("user is created"));
        Assert.assertThat(uid, not(nullValue()));
        Assert.assertThat(uid, is(stubUid));
    }

    @Test(expected = DeterLabOperationFailedException.class)
    public void testJoinProjectNewUsersBad() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join project request new users fail");

        mockServer.expect(requestTo(properties.getJoinProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.joinProjectNewUsers(anyString());
    }

    @Test(expected = JSONException.class)
    public void testJoinProjectNewUsersJsonError() {
        JSONObject userObject = Util.getUserAdapterJSONObject();

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getJoinProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.joinProjectNewUsers(userObject.toString());
    }

    @Test
    public void testCreateProjectNewUsersGood() {
        JSONObject userObject = Util.getUserAdapterJSONObject();

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getApplyProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.applyProjectNewUsers(userObject.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        String uid = resultJSONObject.getString("uid");
        Assert.assertThat(msg, is("user is created"));
        Assert.assertThat(uid, not(nullValue()));
        Assert.assertThat(uid, is(stubUid));
    }

    @Test(expected = JSONException.class)
    public void testCreateProjectNewUsersJsonError() {
        JSONObject userObject = Util.getUserAdapterJSONObject();

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getApplyProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.applyProjectNewUsers(userObject.toString());
    }

    @Test(expected = DeterLabOperationFailedException.class)
    public void testCreateProjectNewUsersBad() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "apply project request new users fail");

        mockServer.expect(requestTo(properties.getApplyProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.applyProjectNewUsers(anyString());
    }

    @Test
    public void testJoinProjectGood() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join project request existing users success");

        mockServer.expect(requestTo(properties.getJoinProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        JSONObject userJoinTeamObject = Util.getTeamAdapterJSONObject();

        adapterDeterLab.joinProject(userJoinTeamObject.toString());
    }

    @Test(expected = JSONException.class)
    public void testJoinProjectJsonError() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("error", "a simple message");

        mockServer.expect(requestTo(properties.getJoinProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        JSONObject userJoinTeamObject = Util.getTeamAdapterJSONObject();

        adapterDeterLab.joinProject(userJoinTeamObject.toString());
    }

    @Test(expected = DeterLabOperationFailedException.class)
    public void testJoinProjectBad() {
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join project request existing users fail");

        mockServer.expect(requestTo(properties.getJoinProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        JSONObject userJoinTeamObject = Util.getTeamAdapterJSONObject();

        adapterDeterLab.joinProject(userJoinTeamObject.toString());
    }

    @Test
    public void testSaveUserOnDeterUserRepository() {
        String nclUserId = RandomStringUtils.randomAlphanumeric(20);
        String deterUserId = RandomStringUtils.randomAlphanumeric(8);

        adapterDeterLab.saveDeterUserIdMapping(deterUserId, nclUserId);
        Assert.assertThat(deterLabUserRepository.findByDeterUserId(deterUserId), not(nullValue()));
        Assert.assertThat(deterUserId, is(deterLabUserRepository.findByDeterUserId(deterUserId).getDeterUserId()));
    }

    @Test(expected = AdapterDeterlabConnectException.class)
    public void testCreateExperimentOnDeter() {
        JSONObject experimentObject = Util.getExperimentAdapterJsonObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment is created");

        mockServer.expect(requestTo(properties.getCreateExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.createExperiment(experimentObject.toString());
    }

    @Test(expected = NSFileParseException.class)
    public void testCreateExperimentNSFIleError() {
        JSONObject experimentObject = Util.getExperimentAdapterJsonObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment create fail ns file error");

        mockServer.expect(requestTo(properties.getCreateExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.createExperiment(experimentObject.toString());
    }

    @Test(expected = ExpNameAlreadyExistsException.class)
    public void testCreateExperimentExpNameExistsError() {
        JSONObject experimentObject = Util.getExperimentAdapterJsonObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment create fail exp name already in use");

        mockServer.expect(requestTo(properties.getCreateExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.createExperiment(experimentObject.toString());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetDeterUserIdBad() {
        adapterDeterLab.getDeterUserIdByNclUserId(RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
    public void testGetDeterUserIdGood() {
        DeterLabUserEntity deterLabUserEntity = Util.getDeterlabUserEntity();
        deterLabUserRepository.saveAndFlush(deterLabUserEntity);
        String expectedDeterUserId = adapterDeterLab.getDeterUserIdByNclUserId(deterLabUserEntity.getNclUserId());
        Assert.assertThat(deterLabUserEntity.getDeterUserId(), is(expectedDeterUserId));
    }

    @Test
    public void testApproveJoinRequestOnDeter() {
        JSONObject one = Util.getApproveJoinRequestAdapterJsonObject();
        one.put("action", "approve");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "process join request OK");

        mockServer.expect(requestTo(properties.getApproveJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.processJoinRequest(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("process join request OK"));
    }

    @Test(expected = JSONException.class)
    public void testApproveJoinRequestOnDeterJsonError() {
        JSONObject one = Util.getApproveJoinRequestAdapterJsonObject();
        one.put("action", "approve");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("message", "process join request OK");

        mockServer.expect(requestTo(properties.getApproveJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.processJoinRequest(one.toString());
    }

    @Test
    public void testApplyProjectGood() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "apply project request existing users success");

        mockServer.expect(requestTo(properties.getApplyProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.applyProject(one.toString());
    }

    @Test
    public void testUpdateCredentialsGood() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "password change success");

        mockServer.expect(requestTo(properties.getUpdateCredentials()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.updateCredentials(one.toString());
    }

    @Test(expected = CredentialsUpdateException.class)
    public void testUpdateCredentialsFail() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "password change fail");

        mockServer.expect(requestTo(properties.getUpdateCredentials()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.updateCredentials(one.toString());
    }

    @Test(expected = JSONException.class)
    public void testUpdateCredentialsJsonError() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("message", "change password");

        mockServer.expect(requestTo(properties.getUpdateCredentials()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.updateCredentials(one.toString());
    }

    @Test(expected = JSONException.class)
    public void testApplyProjectJsonError() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("message", "apply project request existing users success");

        mockServer.expect(requestTo(properties.getApplyProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.applyProject(one.toString());
    }

    @Test(expected = DeterLabOperationFailedException.class)
    public void testApplyProjectBad() throws Exception {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "apply project request existing users fail");

        mockServer.expect(requestTo(properties.getApplyProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.applyProject(one.toString());
    }

    @Test
    public void testApproveProject() {
        JSONObject one = new JSONObject();
        one.put("pid", "11111111");
        one.put("uid", "22222222");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "approve project OK");

        mockServer.expect(requestTo(properties.getApproveProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.approveProject(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("approve project OK"));
    }

    @Test(expected = JSONException.class)
    public void testApproveProjectJsonError() {
        JSONObject one = new JSONObject();
        one.put("pid", "11111111");
        one.put("uid", "22222222");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("message", "approve project OK");

        mockServer.expect(requestTo(properties.getApproveProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.approveProject(one.toString());
    }

    @Test
    public void testRejectProject() {
        JSONObject one = new JSONObject();
        one.put("pid", "11111111");
        one.put("uid", "22222222");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "reject project OK");

        mockServer.expect(requestTo(properties.getRejectProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.rejectProject(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("reject project OK"));
    }

    @Test(expected = JSONException.class)
    public void testRejectProjectJsonError() {
        JSONObject one = new JSONObject();
        one.put("pid", "11111111");
        one.put("uid", "22222222");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("message", "reject project OK");

        mockServer.expect(requestTo(properties.getRejectProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.rejectProject(one.toString());
    }

    @Test
    public void testRejectJoinRequest() {
        JSONObject one = Util.getApproveJoinRequestAdapterJsonObject();
        one.put("action", "deny");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "process join request OK");

        mockServer.expect(requestTo(properties.getRejectJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.processJoinRequest(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("process join request OK"));
    }

    @Test(expected = JSONException.class)
    public void testRejectJoinRequestJsonError() {
        JSONObject one = Util.getApproveJoinRequestAdapterJsonObject();
        one.put("action", "deny");
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("message", "process join request OK");

        mockServer.expect(requestTo(properties.getRejectJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        adapterDeterLab.processJoinRequest(one.toString());
    }

    @Test(expected = ExpStartException.class)
    public void testStartExpBad() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment start fail");

        mockServer.expect(requestTo(properties.startExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.startExperiment(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("experiment start fail"));
    }

    @Test
    public void testStartExpGood() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment start success");

        mockServer.expect(requestTo(properties.startExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.startExperiment(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("experiment start success"));
    }

    @Test
    public void testGetExpStatusGood() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "active");

        mockServer.expect(requestTo(properties.getExpStatus()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.getExperimentStatus(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("status");
        Assert.assertThat(msg, is("active"));
    }

    @Test
    public void testStopExpBad() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "error");

        mockServer.expect(requestTo(properties.stopExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.stopExperiment(one.toString());
        Assert.assertThat(result, is("error"));
    }

    @Test
    public void testStopExpGood() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "swapped");

        mockServer.expect(requestTo(properties.stopExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.stopExperiment(one.toString());
        Assert.assertThat(result, is("swapped"));
    }

    @Test(expected = ExpDeleteException.class)
    public void testDeleteExpBad() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "error");

        mockServer.expect(requestTo(properties.deleteExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.deleteExperiment(one.toString());
        Assert.assertThat(result, is("error"));
    }

    @Test
    public void testDeleteExpGood() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("status", "no experiment found");

        mockServer.expect(requestTo(properties.deleteExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterLab.deleteExperiment(one.toString());
        Assert.assertThat(result, is("no experiment found"));
    }
}
