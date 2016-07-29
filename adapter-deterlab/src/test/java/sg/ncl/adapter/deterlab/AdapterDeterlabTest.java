package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;

import javax.inject.Inject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Te Ye
 */
public class AdapterDeterlabTest extends AbstractTest {

    @Autowired
    private RestOperations restOperations;

    @Inject
    private DeterlabUserRepository deterlabUserRepository;

    @Inject
    private AdapterDeterlab adapterDeterlab;

    @Inject
    private ConnectionProperties properties;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        mockServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    }

    @Test
    public void testJoinProjectNewUsersOnDeter() {
        JSONObject userObject = Util.getUserAdapterJSONObject();

        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is created");
        predefinedResultJson.put("uid", stubUid);

        mockServer.expect(requestTo(properties.getJoinProjectNewUsers()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.joinProjectNewUsers(userObject.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        String uid = resultJSONObject.getString("uid");
        Assert.assertThat(msg, is("user is created"));
        Assert.assertThat(uid, not(nullValue()));
        Assert.assertThat(uid, is(stubUid));
    }

    @Test
    public void testCreateProjectOnDeter() {
        JSONObject teamObject = Util.getTeamAdapterJSONObject();
    }

/*    @Test
    public void testLoginOnDeter() {
        JSONObject userLoginObject = Util.getLoginAdapterJSONObject();
        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user is logged in");

        mockServer.expect(requestTo(properties.getLogin()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String loginResultJSON = adapterDeterlab.loginUsers(userLoginObject.toString());
        JSONObject loginResultJSONObject = new JSONObject(loginResultJSON);
        String loginMsg = loginResultJSONObject.getString("msg");
        Assert.assertThat(loginMsg, is("user is logged in"));
    }*/

    @Test
    public void testJoinProjectOnDeter() {
        // below is actual invocation of the remote join project function
//        JSONObject userLoginObject = new JSONObject();
//        userLoginObject.put("uid", "mickey");
//        userLoginObject.put("password", "deterinavm");
//
//        JSONObject userJoinTeamObject = new JSONObject();
//        userJoinTeamObject.put("uid", "mickey");
//        userJoinTeamObject.put("pid", "NCL");
//
//        String loginResultJSON = adapterDeterlab.loginUsers(userLoginObject.toString());
//        String joinTeamResultJSON = adapterDeterlab.joinProject(userJoinTeamObject.toString());

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user has logged in and joined a project");

        mockServer.expect(requestTo(properties.getJoinProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        JSONObject userJoinTeamObject = Util.getTeamAdapterJSONObject();

        String joinTeamResultJSON = adapterDeterlab.joinProject(userJoinTeamObject.toString());
        JSONObject joinTeamResultJSONObject = new JSONObject(joinTeamResultJSON);
        String joinMsg = joinTeamResultJSONObject.getString("msg");
        Assert.assertThat(joinMsg, is("user has logged in and joined a project"));
    }

    @Test
    public void testSaveUserOnDeterUserRepository() {
        String nclUserId = RandomStringUtils.randomAlphanumeric(20);
        String deterUserId = RandomStringUtils.randomAlphanumeric(8);

        adapterDeterlab.saveDeterUserIdMapping(deterUserId, nclUserId);
        Assert.assertThat(deterlabUserRepository.findByDeterUserId(deterUserId), not(nullValue()));
        Assert.assertThat(deterUserId, is(deterlabUserRepository.findByDeterUserId(deterUserId).getDeterUserId()));
    }

    @Test
    public void testCreateExperimentOnDeter() {
        JSONObject experimentObject = Util.getExperimentAdapterJsonObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "experiment is created");

        mockServer.expect(requestTo(properties.getCreateExperiment()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.createExperiment(experimentObject.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("experiment is created"));
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetDeterUserIdBad() {
        adapterDeterlab.getDeterUserIdByNclUserId(RandomStringUtils.randomAlphanumeric(20));
    }

    @Test
    public void testGetDeterUserIdGood() {
        DeterlabUserEntity deterlabUserEntity = Util.getDeterlabUserEntity();
        deterlabUserRepository.saveAndFlush(deterlabUserEntity);
        String expectedDeterUserId = adapterDeterlab.getDeterUserIdByNclUserId(deterlabUserEntity.getNclUserId());
        Assert.assertThat(deterlabUserEntity.getDeterUserId(), is(expectedDeterUserId));
    }

    @Test
    public void testApproveJoinRequestOnDeter() {
        JSONObject one = Util.getApproveJoinRequestAdapterJsonObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join request approved");

        mockServer.expect(requestTo(properties.getApproveJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.approveJoinRequest(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("join request approved"));
    }

    @Test
    public void testApplyProject() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "user has logged in and applied a project");

        mockServer.expect(requestTo(properties.getApplyProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.applyProject(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("user has logged in and applied a project"));
    }

    @Test
    public void testApproveProject() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "project approved");

        mockServer.expect(requestTo(properties.getApproveProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.approveProject(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("project approved"));
    }

    @Test
    public void testRejectProject() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "project rejected");

        mockServer.expect(requestTo(properties.getRejectProject()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.rejectProject(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("project rejected"));
    }

    @Test
    public void testRejectJoinRequest() {
        JSONObject one = new JSONObject();

        JSONObject predefinedResultJson = new JSONObject();
        predefinedResultJson.put("msg", "join request rejected");

        mockServer.expect(requestTo(properties.getRejectJoinRequest()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(predefinedResultJson.toString(), MediaType.APPLICATION_JSON));

        String result = adapterDeterlab.rejectJoinRequest(one.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        Assert.assertThat(msg, is("join request rejected"));
    }
}
