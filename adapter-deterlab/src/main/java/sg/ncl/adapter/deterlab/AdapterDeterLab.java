package sg.ncl.adapter.deterlab;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabProjectRepository;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabProjectEntity;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.*;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 * Contributors: Vu , Teye
 */
@Component
@Slf4j
public class AdapterDeterLab {

    private DeterLabProjectRepository deterLabProjectRepository;
    private DeterLabUserRepository deterLabUserRepository;
    private ConnectionProperties properties;
    private RestTemplate restTemplate;

    private static final String PID = "pid";
    private static final String UID = "uid";
    private static final String NUM_NODES = "numNodes";
    private static final String STATUS = "status";
    private static final String MACHINE_TYPE = "machineType";

    @Inject
    public AdapterDeterLab(DeterLabUserRepository repository, DeterLabProjectRepository deterLabProjectRepository, ConnectionProperties connectionProperties, RestTemplate restTemplate) {
        this.deterLabUserRepository = repository;
        this.deterLabProjectRepository = deterLabProjectRepository;
        this.properties = connectionProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * Creates a join project request to Deterlab
     * Also creates a new user
     *
     * @param jsonString
     * @return The Deter userid (randomly generated)
     */
    public String joinProjectNewUsers(String jsonString) {
        log.debug("Joining project as new user: {}", jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;
        try {
            response = restTemplate.exchange(properties.getJoinProjectNewUsers(), HttpMethod.POST, request, String.class);
        } catch (ResourceAccessException rae) {
            log.warn("New user join project error: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("New user join project error: Adapter DeterLab internal server error {}", hsee);
            throw hsee;
        }
        // Will get the following JSON:
        // msg: join project request new users fail
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        String responseBody = response.getBody().toString();
        try {
            String deterMessage = new JSONObject(responseBody).getString("msg");
            if("user is created".equalsIgnoreCase(deterMessage)) {
                log.info("Join project as new user to DeterLab OK");
                return responseBody;
            }
            log.warn("Join project new user error: {}", deterMessage);
            throw new DeterLabOperationFailedException(deterMessage);
        } catch (JSONException e) {
            log.warn("Error parsing response code new user join project: {}", responseBody);
            throw e;
        }
    }

    /**
     * Creates a apply project request to Deterlab
     * Also creates a new user
     *
     * @param jsonString
     * @return The Deter userid (randomly generated)
     */
    public String applyProjectNewUsers(String jsonString) {
        log.debug("Applying new project as new user: {}", jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;
        try {
            response = restTemplate.exchange(properties.getApplyProjectNewUsers(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error new user apply project: {}", e);
            throw new AdapterConnectionException();
        }
        // Will get the following JSON:
        // msg: join project request new users fail
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        String responseBody = response.getBody().toString();
        try {

            String deterMessage = new JSONObject(responseBody).getString("msg");
            if("user is created".equalsIgnoreCase(deterMessage)) {
                log.info("Apply project as new user to DeterLab OK");
                return responseBody;
            }
            log.warn("Apply project new user error: {}", deterMessage);
            throw new DeterLabOperationFailedException(deterMessage);
        } catch (JSONException e) {
            log.warn("Error parsing response code new user apply project: {}", responseBody);
            throw e;
        }
    }

    /**
     * Creates a apply project request to Deterlab
     * Does not create a new user
     *
     * @param jsonString Contains uid, project name, pid, project goals, project web, project organisation, project visibility
     */
    public String applyProject(String jsonString) {
        log.info("Applying project as existing user: {}", jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getApplyProject(), HttpMethod.POST, request, String.class);

            final String responseBody = response.getBody().toString();

            log.debug("Apply project as existing user : Deter response -- {}", responseBody);

            String deterMessage = new JSONObject(responseBody).getString("msg");

            if ("apply project existing users ok".equalsIgnoreCase(deterMessage)) {
                log.info("Apply project as existing user : OK");
                return responseBody;
            }
            log.warn("Apply project as existing user : error: {}", deterMessage);
            throw new DeterLabOperationFailedException(deterMessage);

        } catch (ResourceAccessException rae) {
            log.warn("Apply project as existing user : Adapter connection error {}", rae);
            throw new AdapterConnectionException();

        } catch (HttpServerErrorException hsee) {
            log.warn("Apply project as existing user : Adapter internal server error {}", hsee);
            throw new AdapterInternalErrorException();

        } catch (JSONException e) {
            log.warn("Apply project as existing user : error parsing response body");
            throw e;
        }
    }

    /**
     * Creates a join project request to Deterlab
     * Does not create a new user
     *
     * @param jsonString Contains uid, pid
     */
    public String joinProject(String jsonString) {
        log.info("Joining project as existing user : {}", jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getJoinProject(), HttpMethod.POST, request, String.class);

            String responseBody = response.getBody().toString();

            log.debug("Join project as existing user : adapter response body -- {}", responseBody);

            String deterMessage = new JSONObject(responseBody).getString("msg");

            if ("Join project existing user ok".equalsIgnoreCase(deterMessage)) {
                log.info("Join project as existing user : OK");
                return responseBody;
            }
            log.warn("Join project as existing user : error on DeterLab -- {}", deterMessage);
            throw new DeterLabOperationFailedException(deterMessage);

        } catch (ResourceAccessException rae) {
            log.warn("Join project as existing user : adapter connection error {}", rae);
            throw new AdapterConnectionException();

        } catch (HttpServerErrorException hsee) {
            log.warn("Join project as existing user : adapter internal server error {}", hsee);
            throw new AdapterInternalErrorException();

        } catch (JSONException e) {
            log.warn("Join project as existing user : error parsing response body");
            throw e;
        }
    }

    /**
     * Creates a edit user profile request to Deterlab
     *
     * @param jsonString Contains uid, password, confirm password
     */
    public void updateCredentials(String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getUpdateCredentials(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error update credentials: {}", e);
            throw new AdapterConnectionException();
        }

        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if (!"password change success".equals(jsonResult)) {
                log.warn("Change password failed: {}", response.getBody().toString());
                throw new CredentialsUpdateException();
            }
            log.info("Change password OK");
        } catch (JSONException e) {
            log.warn("Error parsing response code update credentials: {}", response.getBody().toString());
            throw e;
        }
    }

    /**
     *
     * @param jsonString {"uid" : "uid",
     *                   "password": "password"
     *                   }
     */
    public void resetPassword(String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getResetPasswordURI(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("Cannot connect to adapter | server internal error: {}", e);
            throw new AdapterConnectionException();
        }

        String msg = new JSONObject(response.getBody().toString()).getString("msg");
        if ("password was reset".equals(msg)) {
            log.info("Password was reset");
            return;
        }

        log.warn("Reset password failed: {}", msg);
        throw new CredentialsUpdateException();
    }

    /**
     * Creates the cookie file on the boss machine due to the timeout issue
     *
     * @param nclUserId The ncl user id is required to retrieve the deter user id
     * @param password  Raw password
     */
    public void login(String nclUserId, String password) {
        if (!properties.isEnabled()) {
            log.info("Bypass DeterLab login");
            return;
        }

        JSONObject adapterObject = new JSONObject();
        adapterObject.put(UID, getDeterUserIdByNclUserId(nclUserId));
        adapterObject.put("password", password);

        log.info("Logging into DeterLab");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(adapterObject.toString(), headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.login(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("Adapter connection error login on deterlab: {}", e);
            throw new AdapterConnectionException();
        }

        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if (!"user is logged in".equals(jsonResult)) {
                log.warn("login failed: {}", response.getBody().toString());
                throw new DeterLabOperationFailedException(jsonResult);
            }
            log.info("login success");
        } catch (JSONException e) {
            log.warn("Error parsing response code login on deterlab: {}", response.getBody().toString());
            throw e;
        }
    }

    @Transactional
    public void saveDeterUserIdMapping(String deterUserId, String nclUserId) {
        DeterLabUserEntity deterLabUserEntity = new DeterLabUserEntity();
        deterLabUserEntity.setNclUserId(nclUserId);
        deterLabUserEntity.setDeterUserId(deterUserId);
        deterLabUserRepository.save(deterLabUserEntity);
    }

    public String getDeterUserIdByNclUserId(String nclUserId) {
        DeterLabUserEntity deterLabUserEntity = deterLabUserRepository.findByNclUserId(nclUserId);
        if (deterLabUserEntity == null) {
            throw new UserNotFoundException(nclUserId);
        }
        return deterLabUserEntity.getDeterUserId();
    }

    // deterProjectId is ncl teamName
    @Transactional
    public DeterLabProjectEntity saveDeterProjectId(String deterProjectId, String nclTeamId) {
        DeterLabProjectEntity deterLabProjectEntity = new DeterLabProjectEntity();
        deterLabProjectEntity.setNclTeamId(nclTeamId);
        deterLabProjectEntity.setDeterProjectId(deterProjectId);
        return deterLabProjectRepository.save(deterLabProjectEntity);
    }

    public String getDeterProjectIdByNclTeamId(String nclTeamId) {
        DeterLabProjectEntity deterLabProjectEntity = deterLabProjectRepository.findByNclTeamId(nclTeamId);
        if (deterLabProjectEntity == null) {
            throw new TeamNotFoundException(nclTeamId);
        }
        return deterLabProjectEntity.getDeterProjectId();
    }

    /**
     * Creates a create experiment request to Deterlab
     *
     * @param jsonString Contains id, userId, teamId, teamName, name (experiment name), description, nsFile, nsFileContent, idleSwap, maxDuration, deterLogin (deter userId), userServerUri
     */
    public void createExperiment(String jsonString) {
        log.info("Creating experiment to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getCreateExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new AdapterConnectionException();
        }

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");

        if ("experiment create fail ns file error".equals(jsonResult)) {
            throw new NSFileParseException(jsonResult);
        } else if ("experiment create fail exp name already in use".equals(jsonResult)) {
            throw new ExperimentNameAlreadyExistsException(jsonResult);
        } else if (!"experiment create success".equals(jsonResult)) {
            throw new AdapterConnectionException();
        }
    }

    /**
     * Creates a start experiment request to Deterlab
     *
     * @param teamName equivalent of pid
     * @param experimentName equivalent of eid
     * @param nclUserId the ncl user id who starts the experiment
     * @return a experiment report if the experiment is started successfully and active, otherwise a "experiment start fail" is return
     * @implNote must return the entire response body as realization service needs to store the experiment report to transmit back to UI
     */
    public String startExperiment(String teamName, String experimentName, String nclUserId) {

        String jsonString = craftJSONStringStartStopExperiment("in", teamName, experimentName, nclUserId);

        log.info("Start experiment - {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.startExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.warn("Adapter error start experiment: {}", e);
            throw new AdapterConnectionException(e.getMessage());
        }

        log.info("Start experiment request submitted to deterlab");
        log.info("Start experiment response from deterlab: {}", response.getBody());
        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");

        if ("experiment start fail".equals(jsonResult)) {
            log.warn("Fail to start experiment at deterlab {}", jsonString);
            throw new ExperimentStartException();
        } else if (!"experiment start success".equals(jsonResult)) {
            log.warn("Start experiment connection error {}", jsonString);
            throw new AdapterConnectionException();
        }

        log.info("Start experiment request success at deterlab {}", response.getBody().toString());
        return response.getBody().toString();
    }

    /**
     * Creates a stop experiment request to Deterlab
     *
     * @param teamName equivalent of pid
     * @param experimentName equivalent of eid
     * @param nclUserId the ncl user id that stops the experiment
     * @return the experiment status
     * @implNote we don't throw any exception if the result returned from deterlab is not "swapped" since there may be other types of experiment status unknown to us
     */
    public String stopExperiment(String teamName, String experimentName, String nclUserId) {

        String jsonString = craftJSONStringStartStopExperiment("out", teamName, experimentName, nclUserId);

        log.info("Stop experiment - {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.stopExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.warn("Adapter error start experiment: {}", e);
            throw new AdapterConnectionException(e.getMessage());
        }

        log.info("Stop experiment request submitted to deterlab");
        String expStatus = new JSONObject(response.getBody().toString()).getString(STATUS);

        if (!"swapped".equals(expStatus)) {
            log.warn("Fail to stop experiment at deterlab {}", jsonString);
        }
        log.info("Stop experiment request success at deterlab {}", response.getBody().toString());
        return expStatus;
    }

    /**
     * Creates a delete experiment request to Deterlab
     *
     * @param teamName equivalent of pid
     * @param experimentName equivalent of eid
     * @param nclUserId the ncl user id who deletes the experiment
     * @return the experiment status
     */
    public String deleteExperiment(String teamName, String experimentName, String nclUserId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("teamName", teamName);
        jsonObject.put("experimentName", experimentName);
        jsonObject.put("deterLogin", getDeterUserIdByNclUserId(nclUserId));

        String jsonString = jsonObject.toString();

        log.info("Delete experiment - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.deleteExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.warn("Adapter error delete experiment: {}", e);
            throw new AdapterConnectionException(e.getMessage());
        }

        log.info("Delete experiment request submitted to deterlab");
        String expStatus = new JSONObject(response.getBody().toString()).getString(STATUS);

        if (!"no experiment found".equals(expStatus)) {
            log.warn("Fail to delete experiment at deterlab {}", jsonString);
            throw new ExperimentDeleteException();
        }

        log.info("Delete experiment request success at deterlab {}", response.getBody().toString());
        return expStatus;
    }

    public String modifyNSFile(String jsonString) {
        log.info("Modify experiment ns file - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.modifyNSFile(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.warn("Adapter error modify experiment ns file: {}", e);
            throw new AdapterConnectionException(e.getMessage());
        }

        log.info("Modify experiment ns file request submitted to deterlab");
        String status = new JSONObject(response.getBody().toString()).getString("msg");
        String modifyOutput = new JSONObject(response.getBody().toString()).getString("modify_experiment");

        if ("modify experiment fail ns file parse error".equals(status)) {
            throw new NSFileParseException(modifyOutput);
        } else if (!"modify experiment success".equals(status)) {
            throw new ExperimentModifyException(modifyOutput);
        }

        log.info("Modify experiment ns file request success at deterlab");
        log.debug("Deter response: {}", response.getBody().toString());
        return status;
    }

    // modify the experiment settings, such as max duration, etc
    public String modifyExperimentSettings(String jsonString) {
        log.info("Modify experiment settings - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.modifyExperimentSettings(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.warn("Adapter error modify experiment settings: {}", e);
            throw new AdapterConnectionException(e.getMessage());
        }

        log.info("Modify experiment settings request submitted to deterlab");
        String status = new JSONObject(response.getBody().toString()).getString("msg");

        if ("modify experiment settings: max_duration fail".equals(status)) {
            throw new ExperimentModifyException("Please select an appropriate shutdown time.");
        }

        log.info("Modify experiment settings request success at deterlab");
        log.debug("Deter response: {}", response.getBody().toString());
        return status;
    }

    /**
     * Retrieves the experiment status from Deterlab
     *
     * @param teamName equivalent of pid
     * @param experimentName equivalent of eid
     * @return the status of the experiment, a "error" if the request fails
     * @implNote cannot throw exception for this method
     */
    public String getExperimentStatus(String teamName, String experimentName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", teamName);
        jsonObject.put("eid", experimentName);
        String jsonString = jsonObject.toString();

        log.info("Get experiment status - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getExpStatus(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.warn("Adapter connection error get experiment status: {}", e);
            return "{\"status\": \"error\"}";
        }

        log.info("Get experiment status request submitted to deterlab");

        return response.getBody().toString();
    }

    public String processJoinRequest(String jsonString) {
        JSONObject request = new JSONObject(jsonString);
        if (request.length() < 5) {
            log.warn("NOT enough inputs: {}", jsonString);
            throw new IllegalArgumentException();
        }
        String pid = request.getString("pid");
        String approverUid = request.getString("approverUid");
        String uid = request.getString(UID);
        String gid = request.getString("gid");
        String action = request.getString("action");
        log.info("Processing join request: team {}, requester {}, approver {}, group {}, action {}",
                pid, uid, approverUid, gid, action);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(jsonString, headers);
        String reqUrl = "approve".equals(action) ? properties.getApproveJoinRequest() : properties.getRejectJoinRequest();

        ResponseEntity httpResponse;
        try {
            httpResponse = restTemplate.exchange(reqUrl, HttpMethod.POST, httpRequest, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error process join request: {}", e);
            throw new AdapterConnectionException();
        }
        String responseBody = httpResponse.getBody().toString();
        try {
            // cases where we want to process join request:
            // case1: normal join request entry exists on deterlab
            // case2: approve a join request not updated on our DB but already approved on deterlab DB
            // case3: reject a join request not updated on our DB but does not exists on deterlab DB
            String adapterResponse = new JSONObject(responseBody).getString("msg");
            log.info("process join request adapter response: {}", adapterResponse);
            if ("process join request OK".equals(adapterResponse) || isJoinRequestValid(action, adapterResponse)) {
                log.info("{} join request to team {} OK", action, pid);
                return responseBody;
            } else {
                log.warn("{} join request to team {} FAIL", action, pid);
                throw new DeterLabOperationFailedException(responseBody);
            }
        } catch (JSONException e) {
            log.warn("Error parsing response code process join request: {}", responseBody);
            throw e;
        }
    }

    private boolean isJoinRequestValid(String action, String adapterResponse) {
        return ("approve".equals(action) && "user is already an approved member in the project".equals(adapterResponse)) ||
                ("deny".equals(action) && "no join request found".equals(adapterResponse));
    }

    public String approveProject(String jsonString) {
        // for ncl admins to approve teams
        String pid = new JSONObject(jsonString).getString("pid");
        log.info("Approving team {}: {}", pid, jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getApproveProject(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error approve project: {}", e);
            throw new AdapterConnectionException();
        }
        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if ("approve project OK".equals(jsonResult)) {
                log.info("Approve team {} OK", pid);
                return response.getBody().toString();
            } else {
                log.warn("Approve team {} FAIL", pid);
                throw new DeterLabOperationFailedException(jsonResult);
            }
        } catch (JSONException e) {
            log.warn("Error parsing response code approve project: {}", response.getBody().toString());
            throw e;
        }
    }

    public String rejectProject(String jsonString) {
        // for ncl admins to reject teams
        String pid = new JSONObject(jsonString).getString("pid");
        log.info("Rejecting team {}: {}", pid, jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getRejectProject(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error reject project: {}", e);
            throw new AdapterConnectionException();
        }
        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if ("reject project OK".equals(jsonResult)) {
                log.info("Reject team {} OK", pid);
                return response.getBody().toString();
            } else {
                log.warn("Reject team {} FAIL", pid);
                throw new DeterLabOperationFailedException(jsonResult);
            }
        } catch (JSONException e) {
            log.warn("Error parsing response code reject project: {}", response.getBody().toString());
            throw e;
        }
    }


    /**
     * information for displaying experiment profile
     * @param jsonString the team name and experiment name
     * @return  a json string in the format:
     *   {
     *       'ns_file' :
     *       {
     *           'msg' : 'success/fail',
     *           'ns_file' : 'ns_file_contents'
     *       },
     *       'realization_details' :
     *       {
     *           'msg' : 'success/fail',
     *           'realization_details' : 'realization_details_contents'
     *       },
     *       'activity_log'	:
     *       {
     *           'msg' : 'success/fail',
     *           'activity_log' : 'activity_log_contents'
     *       }
     *   }
     *   otherwise, returns a '{}'
     */
    public String getExperimentDetails(String jsonString) {
        log.info("Getting experiment details for experiment profile...");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getExpDetails(), HttpMethod.POST, request, String.class);
            String responseBody = response.getBody().toString();

            if (responseBody.contains("ns_file")) {
                log.info("Get experiment details for experiment profile OK");
                return responseBody;
            } else {
                log.warn("Get experiment details for experiment profile FAIL");
            }
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get experiment details for experiment profile: {}", e);
        }
        return "{}";
    }

    /**
     * get list of experiments from a project
     * @param teamId the NCL team ID to get experiments list
     * @return  a json string in the format:
     *  {
     *      "eid1": {"state": "active", "min_nodes": "2", "nodes": "2", "idle_hours": "0", "details": "..."},
     *      "eid2": {"state": "swapped", "min_nodes": "1", "nodes": "0", "idle_hours": "0", "details": ""},
     *      "eid3": {"state": "active", "min_nodes": "2", "nodes": "2", "idle_hours": "0", "details": "..."}
     *   }
     *   otherwise, returns {}
     */
    public String getExperimentsByTeam(String teamId) {
        final String pid = getDeterProjectIdByNclTeamId(teamId);
        log.info("Getting list of experiments for project: {}", pid);

        JSONObject json = new JSONObject();
        json.put("pid", pid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getExperimentsByTeam(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("Adapter connection error: {}", e);
            return "{}";
        }

        log.debug("List of experiments for project {}:\n{}", pid, response.getBody().toString());
        return response.getBody().toString();
    }

    /**
     * get status of an experiment from a project
     * @param teamId the NCL team ID to get experiments list
     * @param expName the experiment name eid
     * @return  a json string in the format:
     *  {
     *      "eid1": {"state": "active", "min_nodes": "2", "nodes": "2", "idle_hours": "0", "details": "..."}
     *   }
     *   otherwise, returns {}
     */
    public String getExperiment(String teamId, String expName) {
        final String pid = getDeterProjectIdByNclTeamId(teamId);
        log.info("Getting experiment status for {}/{}", pid, expName);

        JSONObject json = new JSONObject();
        json.put("pid", pid);
        json.put("eid", expName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getExperiment(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("Adapter connection error: {}", e);
            return "{}";
        }

        log.debug("Status for {}/{}:\n{}", pid, expName, response.getBody().toString());
        return response.getBody().toString();
    }


    public String getFreeNodes() {
        log.info("Getting free nodes...");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getFreeNodes(), HttpMethod.GET, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get free nodes: {}", e);
            return "0";
        }
        return response.getBody().toString();
    }

    public String getNodesStatus() {
        log.info("Getting nodes status...");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getNodesStatus(), HttpMethod.GET, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get nodes status: {}", e);
            return "{}";
        }
        return response.getBody().toString();
    }

    /**
     * @return number of logged in users at the current time
     */
    public String getLoggedInUsersCount() {
        log.info("Getting number of logged in users...");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getLoggedInUsersCount(), HttpMethod.GET, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get number of logged in users: {}", e);
            return "0";
        }
        return response.getBody().toString();
    }

    /**
     * @return number of running experiments
     */
    public String getRunningExperimentsCount() {
        log.info("Getting number of running experiments...");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getRunningExperimentsCount(), HttpMethod.GET, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get number of running experiments: {}", e);
            return "0";
        }
        return response.getBody().toString();
    }

    public String getTotalNodes() {
        log.info("Getting total nodes...");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getTotalNodes(), HttpMethod.GET, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get total nodes: {}", e);
            return "0";
        }
        return response.getBody().toString();
    }

    public String getTopologyThumbnail(String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getTopoThumbnail(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get thumbnail: {}", e);
            throw new AdapterConnectionException();
        }
        return response.getBody().toString();
    }

    public String getUsageStatistics(String teamId, String startDate, String endDate) {
        // start : mm/dd/yy
        // end : mm/dd/yy
        JSONObject tmp = new JSONObject();
        tmp.put("pid", deterLabProjectRepository.findByNclTeamId(teamId).getDeterProjectId());
        tmp.put("start", startDate);
        tmp.put("end", endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(tmp.toString(), headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getUsageStatistics(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get usage statistics: {}", e);
            return "?";
        }
        return response.getBody().toString();
    }

    public String getGlobalImages() {
        log.info("Getting list of global images from the system");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getGlobalImages(), HttpMethod.GET, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error get global images: {}", e);
            return "{}";
        }
        log.info("Get list of global images OK");
        return response.getBody().toString();
    }

    public String getSavedImages(String teamId) {
        final String pid = getDeterProjectIdByNclTeamId(teamId);
        log.info("Getting list of saved images for project: {}", pid);

        JSONObject json = new JSONObject();
        json.put("pid", pid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getSavedImages(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("Adapter connection error get list of saved images by team: {}", e);
            return "{}";
        }

        log.info("Get list of saved images OK, Deter response: {}", response.getBody().toString());

        return response.getBody().toString();
    }

    public String saveImage(String nclTeamId, String nclUserId, String nodeId, String imageName) {
        final String pid = getDeterProjectIdByNclTeamId(nclTeamId);
        final String uid = getDeterUserIdByNclUserId(nclUserId);
        log.info("Saving image: pid {}, uid {}, node ID {}, image name {}", pid, uid, nodeId, imageName);

        JSONObject json = new JSONObject();
        json.put("pid", pid);
        json.put(UID, uid);
        json.put("nodeId", nodeId);
        json.put("imageName", imageName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.saveImage(), HttpMethod.POST, request, String.class);
            String responseBody = response.getBody().toString();
            String deterMessage = new JSONObject(responseBody).getString("msg");

            if ("save image OK".equals(deterMessage)) {
                log.info("Save image OK");
                return responseBody;
            } else {
                log.warn("Save image FAIL: {}", deterMessage);
                throw new DeterLabOperationFailedException(deterMessage);
            }

        } catch (ResourceAccessException rae) {
            log.warn("Save image error: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("Save image error: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    public String deleteImage(String teamId, String userId, String imageName, boolean imageCreator) {
        // uid, pid in deter is the same as sio project and user name
        final String uid = getDeterUserIdByNclUserId(userId);
        final String pid = getDeterProjectIdByNclTeamId(teamId);

        log.info("Deleting image '{}' from uid '{}' of pid '{}'", imageName, uid, pid);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", pid);
        jsonObject.put(UID, uid);
        jsonObject.put("imageName", imageName);

        if (imageCreator) {
            jsonObject.put("isImageCreator", "yes");
        } else {
            jsonObject.put("isImageCreator", "no");
        }

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        try {
            ResponseEntity responseEntity = restTemplate.exchange(properties.deleteImage(), HttpMethod.DELETE, request, String.class);
            String responseBody = responseEntity.getBody().toString();
            String deterMessage = new JSONObject(responseBody).getString("msg");

            Set<String> curlSuccess = new HashSet<>();
            curlSuccess.add("delete image OK from both web and project directory");
            curlSuccess.add("delete image OK from web but there is unknown error when deleting physical image");
            curlSuccess.add("image still in use");

            String exceptionMessage = "Deleting image '" + imageName + "' of team '" +  pid + "' : " + deterMessage;
            String errorMessage = "Error in deleting image '{}' from uid '{}' of pid '{}' : {} ";

            if (curlSuccess.contains(deterMessage)) {
                log.info("Deleting image '{}' from uid '{}' of pid '{}' : {} ", imageName, uid, pid, deterMessage);
                return responseBody;

            } else if ("not the creator".equals(deterMessage) ||
                    "no permission to delete the imageid when executing Curl command".equals(deterMessage)) {
                log.warn(errorMessage , uid, pid, imageName, deterMessage);
                throw new InsufficientPermissionException(exceptionMessage);

            } else if ("required image id is not found when querying database".equals(deterMessage) ||
                    "no creator found when querying database".equals(deterMessage) ||
                    "imageid could not be found when executing Curl command".equals(deterMessage)) {
                log.warn(errorMessage , uid, pid, imageName, deterMessage);
                throw new ImageNotFoundException(exceptionMessage);

            } else {
                log.warn("Error in deleting image '{}' from uid '{}' of pid '{}' : {} ", imageName, uid, pid, deterMessage);
                throw new DeterLabOperationFailedException(exceptionMessage);
            }

        }catch (ResourceAccessException resourceAccessException) {
            log.warn("Deleting image {} from uid {} of pid {} error: {}", imageName, uid, pid, resourceAccessException);
            throw new AdapterConnectionException(resourceAccessException.getMessage());

        } catch (HttpServerErrorException httpServerErrorException) {
            log.warn("Deleting image {} from uid {} of pid {} error: Adapter DeterLab internal server error {}", imageName, uid, pid, httpServerErrorException);
            throw new AdapterInternalErrorException();
        }
    }

    public String removeUserFromTeam(String nclTeamId, String nclUserId, String nclTeamOwnerId) {
        final String pid = getDeterProjectIdByNclTeamId(nclTeamId);
        final String uid = getDeterUserIdByNclUserId(nclUserId);
        final String ownerUid = getDeterUserIdByNclUserId(nclTeamOwnerId);

        log.info("Removing user {} from team {} requested by owner {}", uid, pid, ownerUid);

        JSONObject json = new JSONObject();
        json.put("pid", pid);
        json.put("uidToBeRemoved", uid);
        json.put("ownerUid", ownerUid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.removeUserFromTeam(), HttpMethod.POST, request, String.class);
            String responseBody = response.getBody().toString();
            String deterMessage = new JSONObject(responseBody).getString("msg");

            if ("remove user from team ok".equals(deterMessage)) {
                log.info("Remove user from team OK");
                return responseBody;
            } else {
                log.warn("Remove user from team FAIL");
                throw new DeterLabOperationFailedException(deterMessage);
            }

        } catch (ResourceAccessException rae) {
            log.warn("Remove user from team: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("Remove user from team: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    /**
     * Crafts the JSON string for start/stop experiment
     * @param operation in or out, implies start or stop experiment respectively
     * @param teamName the equivalent of pid
     * @param experimentName the equivalent of eid
     * @param nclUserId the ncl user id that initiate the start/stop experiment
     * @return a JSON string representation of the items to be sent to adapter
     */
    private String craftJSONStringStartStopExperiment(String operation, String teamName, String experimentName, String nclUserId) {
        StringBuilder httpCommand = new StringBuilder();
        httpCommand.append("?inout=" + operation);
        httpCommand.append("&");
        httpCommand.append("pid=" + teamName);
        httpCommand.append("&");
        httpCommand.append("eid=" + experimentName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("httpCommand", httpCommand.toString());
        jsonObject.put("deterLogin", getDeterUserIdByNclUserId(nclUserId));
        jsonObject.put("pid", teamName);
        jsonObject.put("eid", experimentName);

        return jsonObject.toString();
    }

    public String getPublicKeys(String nclUserId) {
        final String uid = getDeterUserIdByNclUserId(nclUserId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(UID, uid);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        try {
            ResponseEntity responseEntity = restTemplate.exchange(properties.getPublicKeys(), HttpMethod.POST, request, String.class);
            return responseEntity.getBody().toString();
        } catch (ResourceAccessException rae) {
            log.warn("Get ssh public keys: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("Get ssh public keys: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    public String addPublicKey(String nclUserId, String pubkey, String passwd) {
        final String uid = getDeterUserIdByNclUserId(nclUserId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(UID, uid);
        jsonObject.put("pubkey", pubkey);
        jsonObject.put("passwd", passwd);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        try {
            ResponseEntity responseEntity = restTemplate.exchange(properties.addPublicKey(), HttpMethod.POST, request, String.class);
            return responseEntity.getBody().toString();
        } catch (ResourceAccessException rae) {
            log.warn("Add ssh public key: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("Add ssh public key: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    public String deletePublicKey(String nclUserId, String publicKeyId) {
        final String uid = getDeterUserIdByNclUserId(nclUserId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(UID, uid);
        jsonObject.put("kid", publicKeyId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        try {
            ResponseEntity responseEntity = restTemplate.exchange(properties.deletePublicKey(), HttpMethod.DELETE, request, String.class);
            return responseEntity.getBody().toString();
        } catch (ResourceAccessException rae) {
            log.warn("Delete ssh public key: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("Delete ssh public key: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    /**
     * Retrieve the list of nodes reserved for a team
     * @param teamId e.g. F12345-G12345-H12345
     * @return  a json string in the format:
     *   {
     *       "status" : "ok/fail",
     *       "reserved": ["pc1", "pc4", "pc2"],
     *       "in_use": [["pc4", "ncltest01", "vnctest"], ["pc2", "testbed-ncl", "thales-poc"]]
     *   }
     */
    public String getReservationStatus(String teamId) {
        final String pid = getDeterProjectIdByNclTeamId(teamId);

        JSONObject json = new JSONObject();
        json.put(PID, pid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getReservationStatus(), HttpMethod.POST, request, String.class);
            String responseBody = response.getBody().toString();
            String status = new JSONObject(responseBody).getString(STATUS);

            if ("OK".equals(status)) {
                log.info("Get reservation for team {} OK", teamId);
                return responseBody;
            } else {
                log.warn("Get reservation for team {} FAIL: {}", teamId, status);
                throw new DeterLabOperationFailedException(status);
            }

        } catch (ResourceAccessException rae) {
            log.warn("Get reservation: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("Get reservation error: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    /**
     * Release all the nodes or a specific number of nodes
     * @param teamId e.g. F12345-G12345-H12345
     * @param numNodes no. of nodes to release; -1 means release all the nodes
     * @return  a json string in the format:
     *   {
     *       'status' : 'ok/fail'
     *       'released' : [node_id_list]
     *   }
     */
    public String releaseNodes(String teamId, Integer numNodes) {
        final String pid = getDeterProjectIdByNclTeamId(teamId);

        JSONObject json = new JSONObject();
        json.put(PID, pid);
        json.put(NUM_NODES, numNodes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.releaseNodes(), HttpMethod.POST, request, String.class);
            // return regardless of success / fail
            return response.getBody().toString();

        } catch (ResourceAccessException rae) {
            log.warn("release reservation: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("release reservation error: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }

    /**
     * Reserve a specific number of nodes or a particular node type
     * @param teamId e.g. F12345-G12345-H12345
     * @param numNodes no. of nodes to reserve; required
     * @param machineType optional; will attempt to reserve X number of nodes with this machine type if specified
     * @return  a json string in the format:
     *   {
     *       'status' : 'ok/fail'
     *       'message' : 'error message'
     *       'reserved' : [node_id_list]
     *   }
     */
    public String reserveNodes(String teamId, Integer numNodes, String machineType) {
        final String pid = getDeterProjectIdByNclTeamId(teamId);

        JSONObject json = new JSONObject();
        json.put(PID, pid);
        json.put(NUM_NODES, numNodes);
        json.put(MACHINE_TYPE, (machineType != null) ? machineType : "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.reserveNodes(), HttpMethod.POST, request, String.class);
            // return regardless of success / fail
            return response.getBody().toString();

        } catch (ResourceAccessException rae) {
            log.warn("node reservation: {}", rae);
            throw new AdapterConnectionException(rae.getMessage());
        } catch (HttpServerErrorException hsee) {
            log.warn("node reservation error: Adapter DeterLab internal server error {}", hsee);
            throw new AdapterInternalErrorException();
        }
    }
}
