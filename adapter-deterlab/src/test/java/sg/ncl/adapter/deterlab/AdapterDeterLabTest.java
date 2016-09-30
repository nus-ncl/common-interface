package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.AbstractTest;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.adapter.deterlab.Util;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Vu
 */
public class AdapterDeterLabTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DeterLabUserRepository deterLabUserRepository;

    @Mock
    private ConnectionProperties properties;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    ResponseEntity response;

    private AdapterDeterLab adapterDeterLab;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp(){
        assertThat(mockingDetails(deterLabUserRepository).isMock()).isTrue();
        assertThat(mockingDetails(properties).isMock()).isTrue();
        assertThat(mockingDetails(restTemplate).isMock()).isTrue();
        adapterDeterLab=new AdapterDeterLab(deterLabUserRepository, properties,restTemplate);
    }


    //no exception thrown
    @Test
    public void JoinProjectNewUsersTest1() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        myobject.put("msg", "user is created");
        myobject.put("uid", stubUid);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual = adapterDeterLab.joinProjectNewUsers(myobject.toString());

        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void JoinProjectNewUsersTest2(){
        JSONObject myobject = new JSONObject();

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
    }

    //throw DeterLabOperationFailedException
    @Test
    public void JoinProjectNewUsersTest3() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        myobject.put("msg", "user not found");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
    }

    //throw JSONException
    @Test
    public void JoinProjectNewUsersTest4() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        myobject.put("msg", "user not found");
        myobject.put("uid", stubUid);

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
    }

    //no Exception thrown
    @Test
    public void applyProjectNewUsersTest1() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        myobject.put("msg", "user is created");
        myobject.put("uid", stubUid);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual = adapterDeterLab.applyProjectNewUsers(myobject.toString());

        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void applyProjectNewUsersTest2() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new AdapterDeterlabConnectException());

        adapterDeterLab.applyProjectNewUsers(myobject.toString());
    }

    //throw DeterLabOperationFailedException
    @Test
    public void applyProjectNewUsersTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "user is not created");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.applyProjectNewUsers(myobject.toString());
    }

    //throw JSONException
    @Test
    public void applyProjectNewUsersTest4() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.applyProjectNewUsers(myobject.toString());
    }

    //no exception thrown
    @Test
    public void ApplyProjectTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.applyProject(myobject.toString());

        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void ApplyProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));

        adapterDeterLab.applyProject(myobject.toString());
    }

    //throw DeterLabOperationFailedException
    @Test
    public void ApplyProjectTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users fail");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.applyProject(myobject.toString());
    }

    //throw JSONException
    @Test
    public void ApplyProjectTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users fail");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.applyProject(myobject.toString());
    }

    //no exception thrown
    @Test
    public void joinProjectTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "join project request existing users success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.joinProject(myobject.toString());

        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void joinProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "join project request existing users success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));

        adapterDeterLab.joinProject(myobject.toString());
    }

    //thrown DeterLabOperationFailedException
    @Test
    public void joinProjectTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "join project request existing users failure");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.joinProject(myobject.toString());
    }

    //throw JSONException
    @Test
    public void joinProjectTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "join project request existing users success");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.joinProject(myobject.toString());
    }

    //no exception thrown
    @Test
    public void updateCredentialsTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.joinProject(myobject.toString());

        assertEquals(actual,myobject.toString());
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void updateCredentialsTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));

        adapterDeterLab.joinProject(myobject.toString());
    }

    //throw DeterLabOperationFailedException
    @Test
    public void updateCredentialsTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change failure");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.joinProject(myobject.toString());
    }

    //throw JSONException
    @Test
    public void updateCredentialsTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change success");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.joinProject(myobject.toString());
    }

    @Test
    public void saveDeterUserIdMappingTest() {
        String deterUserId = "test1";
        String nclUserId = "test2";
        adapterDeterLab.saveDeterUserIdMapping(deterUserId, nclUserId);
        String actual1=deterLabUserRepository.findByDeterUserId(deterUserId).getDeterUserId();
        String actual2=deterLabUserRepository.findByNclUserId(deterUserId).getNclUserId();

        assertEquals("test1",actual1);
        assertEquals("test2",actual2);
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
