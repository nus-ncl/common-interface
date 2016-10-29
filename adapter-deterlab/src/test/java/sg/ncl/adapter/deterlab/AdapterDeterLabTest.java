package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.*;;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabProjectRepository;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Tran Ly Vu
 */
public class AdapterDeterLabTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DeterLabUserRepository deterLabUserRepository;

    @Mock
    private DeterLabProjectRepository deterLabProjectRepository;

    @Mock
    private ConnectionProperties properties;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    ResponseEntity response;

    private AdapterDeterLab adapterDeterLab;


    @Before
    public void setUp(){
        assertThat(mockingDetails(deterLabUserRepository).isMock()).isTrue();
        assertThat(mockingDetails(properties).isMock()).isTrue();
        assertThat(mockingDetails(restTemplate).isMock()).isTrue();
        adapterDeterLab=new AdapterDeterLab(deterLabUserRepository, deterLabProjectRepository, properties,restTemplate);
    }


    //no exception thrown
    @Test
    public void joinProjectNewUsersTest1() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        myobject.put("msg", "user is created");
        myobject.put("uid", stubUid);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual = adapterDeterLab.joinProjectNewUsers(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProjectNewUsers();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void joinProjectNewUsersTest2(){
        JSONObject myobject = new JSONObject();

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).
                thenThrow(new RestClientException(""));

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProjectNewUsers();

    }

    //throw DeterLabOperationFailedException
    @Test
    public void joinProjectNewUsersTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "user not found");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProjectNewUsers();
    }

    //throw JSONException
    @Test
    public void joinProjectNewUsersTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "user not found");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProjectNewUsers();
    }

    //throw EmailAlreadyExistsException
    @Test
    public void joinProjectNewUsersTest5() {
        JSONObject myobject = new JSONObject();
        String stubUid = RandomStringUtils.randomAlphanumeric(8);
        myobject.put("msg", "email address in use");

        exception.expect(EmailAlreadyExistsException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProjectNewUsers();
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

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProjectNewUsers();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void applyProjectNewUsersTest2() {
        JSONObject myobject = new JSONObject();

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new AdapterDeterlabConnectException());

        adapterDeterLab.applyProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProjectNewUsers();
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
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProjectNewUsers();
    }

    //throw JSONException
    @Test
    public void applyProjectNewUsersTest4() {
        JSONObject myobject = new JSONObject();

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        adapterDeterLab.applyProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProjectNewUsers();
    }

    //no exception thrown
    @Test
    public void applyProjectTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.applyProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProject();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void applyProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));
        adapterDeterLab.applyProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProject();

    }

    //throw DeterLabOperationFailedException
    @Test
    public void applyProjectTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users fail");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        adapterDeterLab.applyProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProject();

    }

    //throw JSONException
    @Test
    public void applyProjectTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users fail");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");
        adapterDeterLab.applyProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProject();

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

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProject();
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

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProject();

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

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProject();

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

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProject();
    }

    //no exception thrown
    @Test
    public void updateCredentialsTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        adapterDeterLab.updateCredentials(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getUpdateCredentials();
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void updateCredentialsTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));
        adapterDeterLab.updateCredentials(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getUpdateCredentials();

    }

    //throw CredentialsUpdateException
    @Test
    public void updateCredentialsTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change failure");

        exception.expect(CredentialsUpdateException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        adapterDeterLab.updateCredentials(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getUpdateCredentials();

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
        adapterDeterLab.updateCredentials(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getUpdateCredentials();

    }

    @Test
    public void saveDeterUserIdMappingTest() {
        String deterUserId = "test1";
        String nclUserId = "test2";

        adapterDeterLab.saveDeterUserIdMapping(deterUserId, nclUserId);

        verify(deterLabUserRepository,times(1)).save(any(DeterLabUserEntity.class));
    }

    //throw UserNotFoundException
    @Test
    public void getDeterUserIdByNclUserIdTest1(){
        String nclUserId = "test";

        exception.expect(UserNotFoundException.class);
        when(deterLabUserRepository.findByNclUserId(nclUserId)).thenReturn(null);
        String actual=adapterDeterLab.getDeterUserIdByNclUserId(nclUserId );

        verify(deterLabUserRepository,times(1)).findByNclUserId(nclUserId);
    }

    //no exception thrown
    @Test
    public void getDeterUserIdByNclUserIdTest2(){
        DeterLabUserEntity deterLabUserEntity=new DeterLabUserEntity();
        deterLabUserEntity.setDeterUserId("test1");
        String expected= deterLabUserEntity.getDeterUserId();

        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(deterLabUserEntity);
        String actual=adapterDeterLab.getDeterUserIdByNclUserId("");

        verify(deterLabUserRepository,times(1)).findByNclUserId(anyString());
        assertEquals(expected,actual);
    }

    //no exception thrown
    @Test
    public void createExperimentTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.createExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getCreateExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void createExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RuntimeException());
        adapterDeterLab.createExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getCreateExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }

    //throw NSFileParseException
    @Test
    public void createExperimentTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create fail ns file error");

        exception.expect(NSFileParseException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.createExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getCreateExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }

    //throw ExpNameAlreadyExistsException
    @Test
    public void createExperimentTest4(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create fail exp name already in use");

        exception.expect(ExpNameAlreadyExistsException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.createExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getCreateExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }
    //throw JSONException
    @Test
    public void createExperimentTest5() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create fail");

        exception.expect(AdapterDeterlabConnectException.class);
        when(properties.getCreateExperiment()).thenReturn("");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        adapterDeterLab.createExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getCreateExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }

    //no exception thrown
    @Test
    public void startExperimentTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment start success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual= adapterDeterLab.startExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void startExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RuntimeException());
        String actual=adapterDeterLab.startExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();


    }

    //throw ExpStartException()
    @Test
    public void startExperimentTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment start fail");

        exception.expect(ExpStartException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.startExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }

    //throw AdapterDeterlabConnectException
    @Test
    public void startExperimentTest4(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.startExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }

    //no exception thrown
    @Test
    public void stopExperimentTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "swapped");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual= adapterDeterLab.stopExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).stopExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.getString("status"),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void stopExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RuntimeException());
        String actual=adapterDeterLab.stopExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).stopExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();
    }

    //no exception thrown
    @Test
    public void  deleteExperimentTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "no experiment found");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual= adapterDeterLab.deleteExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).deleteExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.getString("status"),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void  deleteExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "no experiment found");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new  RuntimeException());

        String actual=adapterDeterLab. deleteExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).deleteExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();
    }

    //throw ExpDeleteException
    @Test
    public void  deleteExperimentTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "experiment found");

        exception.expect(ExpDeleteException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.deleteExperiment(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).deleteExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();
    }

    //no exception thrown
    @Test
    public void  getExperimentStatusTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "test string");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual= adapterDeterLab.getExperimentStatus(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getExpStatus();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.toString(),actual);
    }

    //throw Exception
    @Test
    public void getExperimentStatusTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "no experiment found");

        // exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new  RuntimeException());

        String expected="{\"status\": \"error\"}";

        String actual=adapterDeterLab.getExperimentStatus(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getExpStatus();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(expected,actual);
    }

    //throw IllegalArgumentException
    @Test
    public void  processJoinRequestTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("action", "approve");

        exception.expect(IllegalArgumentException.class);
        String actual= adapterDeterLab.processJoinRequest(myobject.toString());
    }

    //"action" is "approve" and throw AdapterDeterlabConnectException
    @Test
    public void processJoinRequestTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RestClientException(""));

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveJoinRequest();
    }

    //"action" is "approve" and throw  DeterLabOperationFailedException
    @Test
    public void processJoinRequestTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");
        myobject.put("msg", "process join request not OK");

        exception.expect( DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveJoinRequest();
    }

    //"action" is "approve" and throw JSONException
    @Test
    public void processJoinRequestTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");
        myobject.put("msg", "process join request OK");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveJoinRequest();
    }

    //"action" is "approve" and no exception thrown
    @Test
    public void processJoinRequestTest5() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");
        myobject.put("msg", "process join OK");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveJoinRequest();
        assertEquals(myobject .toString(),actual);
    }

    //"action" is not "approve" and throw AdapterDeterlabConnectException
    @Test
    public void processJoinRequestTest6() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "reject");

        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RestClientException(""));

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectJoinRequest();
    }

    //"action" is not "approve" and throw DeterLabOperationFailedException
    @Test
    public void processJoinRequestTest7() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "reject");
        myobject.put("msg", "process join request not OK");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectJoinRequest();
    }

    //"action" is not "approve" and throw JSONException
    @Test
    public void processJoinRequestTest8() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "reject");
        myobject.put("msg", "process join request OK");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn("");

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectJoinRequest();
    }

    //"action" is not "approve" and no exception thrown
    @Test
    public void processJoinRequestTest9() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "reject");
        myobject.put("msg", "process join request OK");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectJoinRequest();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void approveProjectTest1(){
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");


        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RestClientException(""));
        String actual=adapterDeterLab.approveProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveProject();
    }

    //throw DeterLabOperationFailedException
    @Test
    public void approveProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("msg", "approve project not OK");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.approveProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveProject();
    }

    //throw JSONException
    @Test
    public void approveProjectTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("msg", "approve project OK");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.approveProject("");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveProject();
    }

    //no exception thrown
    @Test
    public void approveProjectTest4() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("msg", "approve project OK");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.approveProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveProject();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void rejectProjectTest1(){
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");


        exception.expect(AdapterDeterlabConnectException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RestClientException(""));
        String actual=adapterDeterLab.rejectProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectProject();
    }

    //throw DeterLabOperationFailedException
    @Test
    public void rejectProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("msg", "reject project not OK");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.rejectProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectProject();
    }

    //throw JSONException
    @Test
    public void rejectProjectTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("msg", "reject project OK");

        exception.expect(JSONException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.rejectProject("");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectProject();
    }

    //no exception thrown
    @Test
    public void rejectProject4() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("msg", "reject project OK");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.rejectProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectProject();
        assertEquals(myobject.toString(),actual);
    }

    @Test
    public void loginSuccess() throws Exception {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "user is logged in");

        when(properties.isEnabled()).thenReturn(true);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(deterLabUserRepository.findByNclUserId(eq("userId"))).thenReturn(Util.getDeterlabUserEntity());
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.login("userId", "password");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).login();
    }

    @Test
    public void loginFail() throws Exception {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "user is logged in error");

        when(properties.isEnabled()).thenReturn(true);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(deterLabUserRepository.findByNclUserId(eq("userId"))).thenReturn(Util.getDeterlabUserEntity());
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        exception.expect(DeterLabOperationFailedException.class);

        adapterDeterLab.login("userId", "password");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).login();
    }

    @Test
    public void loginAdapterDeterlabConnectionException() throws Exception {
        when(properties.isEnabled()).thenReturn(true);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).
                thenThrow(new RestClientException(""));
        when(deterLabUserRepository.findByNclUserId(eq("userId"))).thenReturn(Util.getDeterlabUserEntity());

        exception.expect(AdapterDeterlabConnectException.class);

        adapterDeterLab.login("userId", "password");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).login();
    }

    @Test
    public void loginJSONException() throws Exception {
        when(properties.isEnabled()).thenReturn(true);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(deterLabUserRepository.findByNclUserId(eq("userId"))).thenReturn(Util.getDeterlabUserEntity());
        when(response.getBody()).thenReturn("");
        when(response.getBody().toString()).thenReturn("");

        exception.expect(JSONException.class);

        adapterDeterLab.login("userId", "password");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).login();
    }

}
