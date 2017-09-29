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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabProjectRepository;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabProjectEntity;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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

    //throw AdapterConnectionException
    @Test
    public void joinProjectNewUsersTest2(){
        JSONObject myobject = new JSONObject();

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).
                thenThrow(new ResourceAccessException(""));

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

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.joinProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProjectNewUsers();
    }

    //throw InvalidPasswordException
    @Test
    public void joinProjectNewUsersTest6() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "invalid password");

        exception.expect(DeterLabOperationFailedException.class);
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

    //throw AdapterConnectionException
    @Test
    public void applyProjectNewUsersTest2() {
        JSONObject myobject = new JSONObject();

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new AdapterConnectionException());

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

    // throw InvalidPasswordException
    @Test
    public void applyProjectNewUsersTest5() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "invalid password");

        exception.expect(DeterLabOperationFailedException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.applyProjectNewUsers(myobject.toString());
        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProjectNewUsers();
    }

    //no exception thrown
    @Test
    public void applyProjectTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project existing users ok");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.applyProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApplyProject();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterConnectionException
    @Test
    public void applyProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "apply project request existing users success");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new ResourceAccessException(""));
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
        myobject.put("msg", "join project existing user ok");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        String actual=adapterDeterLab.joinProject(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getJoinProject();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterConnectionException
    @Test
    public void joinProjectTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "join project existing user ok");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new ResourceAccessException(""));
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

    //throw AdapterConnectionException
    @Test
    public void updateCredentialsTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "password change success");

        exception.expect(AdapterConnectionException.class);
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

    @Test
    public void saveDeterProjectIdTest() {
        String deterProjectId = "test1";
        String nclTeamId = "test2";

        adapterDeterLab.saveDeterProjectId(deterProjectId, nclTeamId);

        verify(deterLabProjectRepository,times(1)).save(any(DeterLabProjectEntity.class));
    }

    @Test
    public void getDeterProjectIdByNclTeamIdTestException() {
        String nclTeamId = "test";

        exception.expect(TeamNotFoundException.class);
        when(deterLabProjectRepository.findByNclTeamId(nclTeamId)).thenReturn(null);
        adapterDeterLab.getDeterProjectIdByNclTeamId(nclTeamId);

        verify(deterLabProjectRepository,times(1)).findByNclTeamId(nclTeamId);
    }

    @Test
    public void getDeterProjectIdByNclTeamIdTestGood(){
        DeterLabProjectEntity deterLabProjectEntity = new DeterLabProjectEntity();
        deterLabProjectEntity.setDeterProjectId("test1");
        String expected = deterLabProjectEntity.getDeterProjectId();

        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(deterLabProjectEntity);
        String actual = adapterDeterLab.getDeterProjectIdByNclTeamId("");

        verify(deterLabProjectRepository,times(1)).findByNclTeamId(anyString());
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

    //throw AdapterConnectionException
    @Test
    public void createExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        exception.expect(AdapterConnectionException.class);
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

    //throw ExperimentNameAlreadyExistsException
    @Test
    public void createExperimentTest4(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create fail exp name already in use");

        exception.expect(ExperimentNameAlreadyExistsException.class);
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

        exception.expect(AdapterConnectionException.class);
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

    @Test
    public void modifyExperimentGood() {
        String random = RandomStringUtils.randomAlphanumeric(20);

        JSONObject myobject = new JSONObject();
        myobject.put("msg", "modify experiment success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String result = adapterDeterLab.modifyExperiment(random);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).modifyExperiment();

        assertThat(result).isEqualTo(myobject.getString("msg"));
    }

    // throw AdapterConnectionException
    @Test
    public void modifyExperimentAdapterConnectionException() {
        String random = RandomStringUtils.randomAlphanumeric(20);

        JSONObject myobject = new JSONObject();
        myobject.put("msg", "modify experiment fail");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RuntimeException());

        adapterDeterLab.modifyExperiment(random);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).modifyExperiment();
    }

    // throw NSFileParseException
    @Test
    public void modifyExperimentNSFileParseException() {
        String random = RandomStringUtils.randomAlphanumeric(20);

        JSONObject myobject = new JSONObject();
        myobject.put("msg", "modify experiment fail ns file parse error");

        exception.expect(NSFileParseException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.modifyExperiment(random);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).modifyExperiment();
    }

    // throw ExperimentModifyException
    @Test
    public void modifyExperimentExperimentModifyException() {
        String random = RandomStringUtils.randomAlphanumeric(20);

        JSONObject myobject = new JSONObject();
        myobject.put("msg", "modify experiment fail");

        exception.expect(ExperimentModifyException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        adapterDeterLab.modifyExperiment(random);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).modifyExperiment();
    }

    //no exception thrown
    @Test
    public void startExperimentTest1() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment start success");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual= adapterDeterLab.startExperiment("teamName", "experimentName", "nclUserId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterConnectionException
    @Test
    public void startExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RuntimeException());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual=adapterDeterLab.startExperiment("teamName", "experimentName", "nclUserId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();


    }

    //throw ExperimentStartException()
    @Test
    public void startExperimentTest3(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment start fail");

        exception.expect(ExperimentStartException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual=adapterDeterLab.startExperiment("teamName", "experimentName", "nclUserId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).startExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

    }

    //throw AdapterConnectionException
    @Test
    public void startExperimentTest4(){
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual=adapterDeterLab.startExperiment("teamName", "experimentName", "nclUserId");

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
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual= adapterDeterLab.stopExperiment("teamName", "experimentName", "nclUserId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).stopExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.getString("status"),actual);
    }

    //throw AdapterConnectionException
    @Test
    public void stopExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "experiment create success");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new RuntimeException());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual=adapterDeterLab.stopExperiment("teamName", "experimentName", "nclUserId");

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
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual= adapterDeterLab.deleteExperiment("teamName", "experimentName", "nclUserId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).deleteExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();

        assertEquals(myobject.getString("status"),actual);
    }

    //throw AdapterConnectionException
    @Test
    public void  deleteExperimentTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "no experiment found");

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new  RuntimeException());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual=adapterDeterLab. deleteExperiment("teamName", "experimentName", "nclUserId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).deleteExperiment();
        verify(properties,times(1)).getIp();
        verify(properties,times(1)).getPort();
    }

    //throw ExperimentDeleteException
    @Test
    public void  deleteExperimentTest3() {
        JSONObject myobject = new JSONObject();
        myobject.put("status", "experiment found");

        exception.expect(ExperimentDeleteException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String actual=adapterDeterLab.deleteExperiment("teamName", "experimentName", "nclUserId");

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

        String actual= adapterDeterLab.getExperimentStatus("teamName", "experimentName");

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

        // exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenThrow(new  RuntimeException());

        String expected="{\"status\": \"error\"}";

        String actual=adapterDeterLab.getExperimentStatus("teamName", "experimentName");

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

    //"action" is "approve" and throw AdapterConnectionException
    @Test
    public void processJoinRequestTest2() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");

        exception.expect(AdapterConnectionException.class);
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

    //"action" is not "approve" and throw AdapterConnectionException
    @Test
    public void processJoinRequestTest6() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "deny");

        exception.expect(AdapterConnectionException.class);
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
        myobject.put("action", "deny");
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
        myobject.put("action", "deny");
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
        myobject.put("action", "deny");
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

    //throw AdapterConnectionException
    //"action" is approve and "msg" is user is already an approved member in the project
    @Test
    public void processJoinRequestTest10() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");
        myobject.put("msg", "user is already an approved member in the project");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveJoinRequest();
        assertEquals(myobject.toString(),actual);
    }

    //"action" is deny and "msg" is user is already an approved member in the project
    // expect to throw deterlab fail operation
    @Test
    public void processJoinRequestTest11() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "deny");
        myobject.put("msg", "user is already an approved member in the project");

        exception.expect(DeterLabOperationFailedException.class);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectJoinRequest();
        assertEquals(myobject.toString(),actual);
    }

    //"action" is deny and "msg" no join request found
    @Test
    public void processJoinRequestTest12() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "deny");
        myobject.put("msg", "no join request found");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getRejectJoinRequest();
        assertEquals(myobject.toString(),actual);
    }

    //"action" is approve and "msg" no join request found
    // expect to throw deterlab
    @Test
    public void processJoinRequestTest13() {
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");
        myobject.put("approverUid", "");
        myobject.put("uid", "");
        myobject.put("gid", "");
        myobject.put("action", "approve");
        myobject.put("msg", "no join request found");

        exception.expect(DeterLabOperationFailedException.class);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String actual=adapterDeterLab.processJoinRequest(myobject.toString());

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getApproveJoinRequest();
        assertEquals(myobject.toString(),actual);
    }

    //throw AdapterDeterlabConnectException
    @Test
    public void approveProjectTest1(){
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");


        exception.expect(AdapterConnectionException.class);
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

    //throw AdapterConnectionException
    @Test
    public void rejectProjectTest1(){
        JSONObject myobject = new JSONObject();
        myobject.put("pid", "");


        exception.expect(AdapterConnectionException.class);
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

        exception.expect(AdapterConnectionException.class);

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


    //throw AdapterConnectionException
    @Test
    public void getTopologyThumbnailTest1() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);

        exception.expect(AdapterConnectionException.class);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).
                thenThrow(new RestClientException(""));

        String actual = adapterDeterLab.getTopologyThumbnail(randomString);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getTopoThumbnail();
    }

    @Test
    public void getTopologyThumbnailTest2() {
        String randomString1 = RandomStringUtils.randomAlphanumeric(10);
        String expected = RandomStringUtils.randomAlphanumeric(10);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(expected);

        String actual = adapterDeterLab.getTopologyThumbnail(randomString1);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getTopoThumbnail();
        assertThat(actual).isEqualTo(expected);
    }

    //throw RestClientException
    @Test
    public void getUsageStatisticsTest1() {
        String randomTeamId = RandomStringUtils.randomAlphanumeric(10);
        String randomStartDate= RandomStringUtils.randomAlphanumeric(10);
        String randomEndDate = RandomStringUtils.randomAlphanumeric(10);

        DeterLabProjectEntity deterLabProjectEntity = new DeterLabProjectEntity();

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).
                thenThrow(new RestClientException(""));
        when(deterLabProjectRepository.findByNclTeamId(randomTeamId)).thenReturn(deterLabProjectEntity);

        String actual = adapterDeterLab.getUsageStatistics(randomTeamId, randomStartDate, randomEndDate);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getUsageStatistics();
        assertThat(actual).isEqualTo("?");
    }

    @Test
    public void getUsageStatisticsTest2() {
        String randomTeamId = RandomStringUtils.randomAlphanumeric(10);
        String randomStartDate= RandomStringUtils.randomAlphanumeric(10);
        String randomEndDate = RandomStringUtils.randomAlphanumeric(10);
        String expected = RandomStringUtils.randomAlphanumeric(10);

        DeterLabProjectEntity deterLabProjectEntity = new DeterLabProjectEntity();

        when(deterLabProjectRepository.findByNclTeamId(randomTeamId)).thenReturn(deterLabProjectEntity);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).
                thenReturn(response);
        when(response.getBody()).thenReturn(expected);

        String actual = adapterDeterLab.getUsageStatistics(randomTeamId, randomStartDate, randomEndDate);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getUsageStatistics();
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void getGlobalImagesTestBad() {
        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));

        String result = adapterDeterLab.getGlobalImages();

        assertThat(result).isEqualTo("{}");
    }

    @Test
    public void getGlobalImagesGood() {
        JSONObject myobject = new JSONObject();
        myobject.put("imageA", "os, description");
        myobject.put("imageB", "os, description");
        myobject.put("imageC", "os, description");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String result = adapterDeterLab.getGlobalImages();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getGlobalImages();
        assertThat(result).isEqualTo(myobject.toString());
    }

    @Test
    public void getSavedImagesTestBad() {
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException(""));

        String result = adapterDeterLab.getSavedImages("teamId");

        assertThat(result).isEqualTo("{}");
    }

    @Test
    public void getSavedImagesGood() {
        JSONObject myobject = new JSONObject();
        myobject.put("imageA", "created");
        myobject.put("imageB", "saving");
        myobject.put("imageC", "failed");

        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());

        String result = adapterDeterLab.getSavedImages("teamId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getSavedImages();
        assertThat(result).isEqualTo(myobject.toString());
    }

    @Test
    public void saveImageGood() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "save image OK");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String result = adapterDeterLab.saveImage("nclTeamId", "nclUserId", "nodeId", "imageName", "currentOS");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).saveImage();
        assertThat(result).isEqualTo(myobject.toString());
    }

    //throw AdapterConnectionException
    @Test
    public void saveImageAdapterDeterLabConnectionFailed() {

        exception.expect(AdapterConnectionException.class);
        exception.expectMessage(is(equalTo("rae")));

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new ResourceAccessException("rae"));
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        adapterDeterLab.saveImage("nclTeamId", "nclUserId", "nodeId", "imageName", "currentOS");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).saveImage();
    }

    //throw HttpServerErrorException
    @Test
    public void saveImageAdapterInternalErrorException() {

        exception.expect(AdapterInternalErrorException.class);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        adapterDeterLab.saveImage("nclTeamId", "nclUserId", "nodeId", "imageName", "currentOS");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).saveImage();
    }

    // throw DeterLabOperationFailedException
    @Test
    public void saveImageAdapterDeterLabOperationFailedException() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "image name invalid characters");


        exception.expect(DeterLabOperationFailedException.class);
        exception.expectMessage(is(equalTo("image name invalid characters")));

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(response.getBody().toString()).thenReturn(myobject.toString());
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        adapterDeterLab.saveImage("nclTeamId", "nclUserId", "nodeId", "imageName", "currentOS");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).saveImage();
    }

    @Test
    public void getFreeNodesGood() {

        String freeNodes = RandomStringUtils.randomNumeric(3);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(freeNodes);

        String result = adapterDeterLab.getFreeNodes();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getFreeNodes();
        assertThat(result).isEqualTo(freeNodes);
    }

    @Test
    public void getFreeNodesDeterLabConnectionFailed() {

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenThrow(new RestClientException("error get free nodes"));

        String result = adapterDeterLab.getFreeNodes();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getFreeNodes();
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void removeUserFromTeamGood() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "remove user from team ok");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        String result = adapterDeterLab.removeUserFromTeam("teamId", "userId", "ownerId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).removeUserFromTeam();
        assertThat(result).isEqualTo(myobject.toString());
    }

    @Test
    public void removeUserFromTeamDeterLabOperationFailed() {
        JSONObject myobject = new JSONObject();
        myobject.put("msg", "remove user from team fail");

        exception.expect(DeterLabOperationFailedException.class);
        exception.expectMessage(is(equalTo("remove user from team fail")));

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject.toString());
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        adapterDeterLab.removeUserFromTeam("teamId", "userId", "ownerId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).removeUserFromTeam();
    }

    @Test
    public void removeUserFromTeamAdapterConnectionException() {
        exception.expect(AdapterConnectionException.class);
        exception.expectMessage(is(equalTo("rae")));

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new ResourceAccessException("rae"));
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        adapterDeterLab.removeUserFromTeam("teamId", "userId", "ownerId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).removeUserFromTeam();
    }

    @Test
    public void removeUserFromTeamAdapterInternalErrorException() {
        exception.expect(AdapterInternalErrorException.class);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        when(deterLabProjectRepository.findByNclTeamId(anyString())).thenReturn(new DeterLabProjectEntity());
        when(deterLabUserRepository.findByNclUserId(anyString())).thenReturn(new DeterLabUserEntity());

        adapterDeterLab.removeUserFromTeam("teamId", "userId", "ownerId");

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).removeUserFromTeam();
    }

    @Test
    public void getExperimentDetailsGood() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);

        JSONObject myobject = new JSONObject();
        myobject.put("ns_file", "{ 'msg' : 'get ns file success', 'ns_file' : 'ns_file_contents' }");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject);

        String result = adapterDeterLab.getExperimentDetails(randomString);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getExpDetails();
        assertThat(result).isEqualTo(myobject.toString());
    }

    @Test
    public void getExperimentDetailsBadResult() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);

        JSONObject myobject = new JSONObject();
        myobject.put("bad_result", "{ bad_result }");

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(myobject);

        String result = adapterDeterLab.getExperimentDetails(randomString);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getExpDetails();
        assertThat(result).isEqualTo("{}");
    }

    @Test
    public void getExperimentDetailsDeterLabConnectionFailed() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class))).thenThrow(new RestClientException("error get number of logged in users"));

        String result = adapterDeterLab.getExperimentDetails(randomString);

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.POST),anyObject(),eq(String.class));
        verify(properties,times(1)).getExpDetails();
        assertThat(result).isEqualTo("{}");
    }

    @Test
    public void getLoggedInUsersCountGood() {

        String nodes = RandomStringUtils.randomNumeric(3);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(nodes);

        String result = adapterDeterLab.getLoggedInUsersCount();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getLoggedInUsersCount();
        assertThat(result).isEqualTo(nodes);
    }

    @Test
    public void getLoggedInUsersCountDeterLabConnectionFailed() {

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenThrow(new RestClientException("error get number of logged in users"));

        String result = adapterDeterLab.getLoggedInUsersCount();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getLoggedInUsersCount();
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void getRunningExperimentsCountGood() {

        String nodes = RandomStringUtils.randomNumeric(3);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(nodes);

        String result = adapterDeterLab.getRunningExperimentsCount();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getRunningExperimentsCount();
        assertThat(result).isEqualTo(nodes);
    }

    @Test
    public void getRunningExperimentsCountDeterLabConnectionFailed() {

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenThrow(new RestClientException("error get number of running experiments"));

        String result = adapterDeterLab.getRunningExperimentsCount();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getRunningExperimentsCount();
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void getNodesStatusGood() {
        String status = "{ \"type\" : [ { \"status\" : \"up\" } ] }";

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(status);

        String result = adapterDeterLab.getNodesStatus();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getNodesStatus();
        assertThat(result).isEqualTo(status);
    }

    @Test
    public void getNodesStatusDeterLabConnectionFailed() {
        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenThrow(new RestClientException("error get nodes status"));

        String result = adapterDeterLab.getNodesStatus();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getNodesStatus();
        assertThat(result).isEqualTo("{}");
    }

    @Test
    public void getTotalNodesGood() {

        String nodes = RandomStringUtils.randomNumeric(3);

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenReturn(response);
        when(response.getBody()).thenReturn(nodes);

        String result = adapterDeterLab.getTotalNodes();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getTotalNodes();
        assertThat(result).isEqualTo(nodes);
    }

    @Test
    public void getTotalNodesDeterLabConnectionFailed() {

        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class))).thenThrow(new RestClientException("error get free nodes"));

        String result = adapterDeterLab.getTotalNodes();

        verify(restTemplate,times(1)).exchange(anyString(),eq(HttpMethod.GET),anyObject(),eq(String.class));
        verify(properties,times(1)).getTotalNodes();
        assertThat(result).isEqualTo("0");
    }

}