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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabProjectRepository;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabProjectEntity;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.*;

import javax.inject.Inject;

/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 */
@Component
@Slf4j
public class AdapterDeterLab {

    private DeterLabProjectRepository deterLabProjectRepository;
    private DeterLabUserRepository deterLabUserRepository;
    private ConnectionProperties properties;
    private RestTemplate restTemplate;

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
        } catch (RestClientException e) {
            log.warn("DeterLab connection error new user join project: {}", e);
            throw new AdapterDeterlabConnectException();
        }
        // Will get the following JSON:
        // msg: join project request new users fail
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        String responseBody = response.getBody().toString();
        try {
            checkAdapterResultNewUsers(responseBody, "Join new project as new user failed: {}.");

            log.info("Join project as new user to DeterLab OK");
            return responseBody;
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
            throw new AdapterDeterlabConnectException();
        }
        // Will get the following JSON:
        // msg: join project request new users fail
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        String responseBody = response.getBody().toString();
        try {
            checkAdapterResultNewUsers(responseBody, "Apply new project as new user failed: {}.");

            log.info("Apply project as new user to DeterLab OK");
            return responseBody;
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
        } catch (RestClientException e) {
            log.warn("DeterLab connection error apply project existing user: {}", e);
            throw new AdapterDeterlabConnectException();
        }
        String responseBody = response.getBody().toString();
        try {
            String jsonResult = new JSONObject(responseBody).getString("msg");
            if (!"apply project request existing users success".equals(jsonResult)) {
                log.warn("Apply project as existing user to DeterLab failed: {}", responseBody);
                throw new DeterLabOperationFailedException();
            }
            log.info("Apply project as existing user to DeterLab OK");
            return responseBody;
        } catch (JSONException e) {
            log.warn("Error parsing response code existing user apply project: {}", responseBody);
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
        log.info("Joining project as existing user: {}", jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getJoinProject(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error join project existing user: {}", e);
            throw new AdapterDeterlabConnectException();
        }
        String responseBody = response.getBody().toString();
        try {
            String jsonResult = new JSONObject(responseBody).getString("msg");
            if (!"join project request existing users success".equals(jsonResult)) {
                log.warn("Join project as existing user to DeterLab failed: {}", responseBody);
                throw new DeterLabOperationFailedException();
            }
            log.info("Join project as existing user to DeterLab OK");
            return responseBody;
        } catch (JSONException e) {
            log.warn("Error parsing response code existing user join project: {}", responseBody);
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
            throw new AdapterDeterlabConnectException();
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
            throw new AdapterDeterlabConnectException();
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
            log.info("Bypass login");
            return;
        }

        JSONObject adapterObject = new JSONObject();
        adapterObject.put("uid", getDeterUserIdByNclUserId(nclUserId));
        adapterObject.put("password", password);

        log.info("Now attempting to invoke adapter to login and create cookie file");

        log.info("Login on deterlab");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(adapterObject.toString(), headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.login(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("DeterLab connection error login on deterlab: {}", e);
            throw new AdapterDeterlabConnectException();
        }

        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if (!"user is logged in".equals(jsonResult)) {
                log.warn("login failed: {}", response.getBody().toString());
                throw new DeterLabOperationFailedException();
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

    @Transactional
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

    @Transactional
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
            throw new AdapterDeterlabConnectException();
        }

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");

        if ("experiment create fail ns file error".equals(jsonResult)) {
            throw new NSFileParseException();
        } else if ("experiment create fail exp name already in use".equals(jsonResult)) {
            throw new ExpNameAlreadyExistsException();
        } else if (!"experiment create success".equals(jsonResult)) {
            throw new AdapterDeterlabConnectException();
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
            throw new AdapterDeterlabConnectException(e.getMessage());
        }

        log.info("Start experiment request submitted to deterlab");
        log.info("Start experiment response from deterlab: {}", response.getBody());
        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");

        if ("experiment start fail".equals(jsonResult)) {
            log.warn("Fail to start experiment at deterlab {}", jsonString);
            throw new ExpStartException();
        } else if (!"experiment start success".equals(jsonResult)) {
            log.warn("Start experiment connection error {}", jsonString);
            throw new AdapterDeterlabConnectException();
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
            throw new AdapterDeterlabConnectException(e.getMessage());
        }

        log.info("Stop experiment request submitted to deterlab");
        String expStatus = new JSONObject(response.getBody().toString()).getString("status");

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
            throw new AdapterDeterlabConnectException(e.getMessage());
        }

        log.info("Delete experiment request submitted to deterlab");
        String expStatus = new JSONObject(response.getBody().toString()).getString("status");

        if (!"no experiment found".equals(expStatus)) {
            log.warn("Fail to delete experiment at deterlab {}", jsonString);
            throw new ExpDeleteException();
        }

        log.info("Delete experiment request success at deterlab {}", response.getBody().toString());
        return expStatus;
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
        String uid = request.getString("uid");
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
            throw new AdapterDeterlabConnectException();
        }
        String responseBody = httpResponse.getBody().toString();
        try {
            // cases where we want to process join request:
            // case1: normal join request entry exists on deterlab
            // case2: approve a join request not updated on our DB but already approved on deterlab DB
            // case3: reject a join request not updated on our DB but does not exists on deterlab DB
            String adapterResponse = new JSONObject(responseBody).getString("msg");
            log.info("process join request adapter response: {}", adapterResponse);
            if ("process join request OK".equals(adapterResponse) ||
                    ("approve".equals(action) && "user is already an approved member in the project".equals(adapterResponse)) ||
                    ("reject".equals(action) && "no join request found".equals(adapterResponse)) ) {
                log.info("{} join request to team {} OK", action, pid);
                return responseBody;
            } else {
                log.warn("{} join request to team {} FAIL", action, pid);
                throw new DeterLabOperationFailedException();
            }
        } catch (JSONException e) {
            log.warn("Error parsing response code process join request: {}", responseBody);
            throw e;
        }
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
            throw new AdapterDeterlabConnectException();
        }
        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if ("approve project OK".equals(jsonResult)) {
                log.info("Approve team {} OK", pid);
                return response.getBody().toString();
            } else {
                log.warn("Approve team {} FAIL", pid);
                throw new DeterLabOperationFailedException();
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
            throw new AdapterDeterlabConnectException();
        }
        try {
            String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
            if ("reject project OK".equals(jsonResult)) {
                log.info("Reject team {} OK", pid);
                return response.getBody().toString();
            } else {
                log.warn("Reject team {} FAIL", pid);
                throw new DeterLabOperationFailedException();
            }
        } catch (JSONException e) {
            log.warn("Error parsing response code reject project: {}", response.getBody().toString());
            throw e;
        }
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
            throw new AdapterDeterlabConnectException();
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

    /**
     * Checks the response from adapter deterlab when applying or joining a new project as a new user and determines the exception to be thrown
     * Use only if @applyProjectNewUsers and @joinProjectNewUsers are performing identical checks
     * @param responseBody the JSON response from adapter deterlab python script
     * @param logPrefix the prefix for the log messages to display either "Apply new project as new user..." or "Join new project as new user..."
     */
    private void checkAdapterResultNewUsers(String responseBody, String logPrefix) {
        String jsonResult = new JSONObject(responseBody).getString("msg");

        if ("email address in use".equals(jsonResult)) {
            log.warn(logPrefix + " Email address already exists.", responseBody);
            throw new EmailAlreadyExistsException();
        } else if ("invalid password".equals(jsonResult)) {
            log.warn(logPrefix + " Password is invalid.", responseBody);
            throw new InvalidPasswordException();
        } else if (!"user is created".equals(jsonResult)) {
            log.warn(logPrefix, responseBody);
            throw new DeterLabOperationFailedException();
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
}
