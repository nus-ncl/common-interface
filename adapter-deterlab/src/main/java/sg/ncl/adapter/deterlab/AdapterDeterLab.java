package sg.ncl.adapter.deterlab;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterLabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterLabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.*;

import javax.inject.Inject;

/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 */
@Component
public class AdapterDeterLab {

    private DeterLabUserRepository deterLabUserRepository;
    private ConnectionProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(AdapterDeterLab.class);

    @Inject
    private RestTemplate restTemplate;

    @Inject
    public AdapterDeterLab(DeterLabUserRepository repository, ConnectionProperties connectionProperties) {
        this.deterLabUserRepository = repository;
        this.properties = connectionProperties;
    }

    /**
     * Creates a join project request to Deterlab
     * Also creates a new user
     * @param jsonString
     * @return The Deter userid (randomly generated)
     */
    public String joinProjectNewUsers(String jsonString) {

        logger.info("Joining project as new user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;
        try {
            response = restTemplate.exchange(properties.getJoinProjectNewUsers(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new AdapterDeterlabConnectException();
        }

        logger.info("Join project request (new user) submitted to deterlab");

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        if ("join project request new users fail".equals(jsonResult) || !"user is created".equals(jsonResult)) {
            throw new JoinProjectException();
        }

        // Will return the following JSON:
        // msg: join project request new users fail
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        return response.getBody().toString();
    }

    /**
     * Creates a apply project request to Deterlab
     * Also creates a new user
     * @param jsonString
     * @return The Deter userid (randomly generated)
     */
    public String applyProjectNewUsers(String jsonString) {
        logger.info("Applying new project as new user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;
        try {
            response = restTemplate.exchange(properties.getApplyProjectNewUsers(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new AdapterDeterlabConnectException();
        }

        logger.info("Apply project request (new user) submitted to deterlab");

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        if ("apply project request new users fail".equals(jsonResult) || !"user is created".equals(jsonResult)) {
            throw new ApplyNewProjectException();
        }

        // Will return the following JSON:
        // msg: apply project request new users fail
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        return response.getBody().toString();
    }

    /**
     * Creates a apply project request to Deterlab
     * Does not create a new user
     * @param jsonString Contains uid, project name, pid, project goals, project web, project organisation, project visibility
     */
    public void applyProject(String jsonString) {
        logger.info("Applying project as logged on user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getApplyProject(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new AdapterDeterlabConnectException();
        }

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        logger.info("Apply new project request existing users submitted to deterlab: {}", jsonResult);
        if (!"apply project request existing users success".equals(jsonResult)) {
            throw new ApplyNewProjectException();
        }
    }

    /**
     * Creates a join project request to Deterlab
     * Does not create a new user
     * @param jsonString Contains uid, pid
     */
    public void joinProject(String jsonString) {
        logger.info("Joining project as logged on user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getJoinProject(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new AdapterDeterlabConnectException();
        }

        logger.info("Join project request submitted to deterlab");

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        if (!"join project request existing users success".equals(jsonResult)) {
            throw new JoinProjectException();
        }
    }

    /**
     * Creates a edit user profile request to Deterlab
     * @param jsonString Contains uid, password, confirm password
     */
    public void updateCredentials(String jsonString) {
        logger.info("Updating credentials to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getUpdateCredentials(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new AdapterDeterlabConnectException();
        }

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        if ("password change fail".equals(jsonResult)) {
            throw new CredentialsUpdateException();
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
            throw new UserNotFoundException();
        }
        return deterLabUserEntity.getDeterUserId();
    }

    public void createExperiment(String jsonString) {
        logger.info("Creating experiment to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

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
     * @implNote must return the entire response body as realization service needs to store the experiment report to transmit back to UI
     * @param jsonString Contains pid, eid, and deterlab userId
     * @return a experiment report if the experiment is started successfully and active, otherwise a "experiment start fail" is return
     */
    public String startExperiment(String jsonString) {
        logger.info("Start experiment - {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.startExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            logger.warn("Adapter error start experiment: {}", e);
            throw new AdapterDeterlabConnectException(e.getMessage());
        }

        logger.info("Start experiment request submitted to deterlab");
        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");

        if ("experiment start fail".equals(jsonResult)) {
            logger.warn("Fail to start experiment at deterlab {}", jsonString);
            throw new ExpStartException();
        } else if (!"experiment start success".equals(jsonResult)) {
            logger.warn("Start experiment connection error {}", jsonString);
            throw new AdapterDeterlabConnectException();
        }

        logger.info("Start experiment request success at deterlab", response.getBody().toString());
        return response.getBody().toString();
    }

    /**
     * Creates a stop experiment request to Deterlab
     * @implNote we don't throw any exception if the result returned from deterlab is not "swapped" since there may be other types of experiment status unknown to us
     * @param jsonString Contains pid, eid, and deterlab userId
     * @return the experiment status
     */
    public String stopExperiment(String jsonString) {
        logger.info("Stop experiment - {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.stopExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            logger.warn("Adapter error start experiment: {}", e);
            throw new AdapterDeterlabConnectException(e.getMessage());
        }

        logger.info("Stop experiment request submitted to deterlab");
        String expStatus = new JSONObject(response.getBody().toString()).getString("status");

        if (!"swapped".equals(expStatus)) {
            logger.warn("Fail to stop experiment at deterlab {}", jsonString);
        }
        logger.info("Stop experiment request success at deterlab", response.getBody().toString());
        return expStatus;
    }

    /**
     * Creates a delete experiment request to Deterlab
     * @param jsonString Contains experiment name, team name and deterlab userid
     * @return the experiment status
     */
    public String deleteExperiment(String jsonString) {
        logger.info("Delete experiment - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.deleteExperiment(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            logger.warn("Adapter error delete experiment: {}", e);
            throw new AdapterDeterlabConnectException(e.getMessage());
        }

        logger.info("Delete experiment request submitted to deterlab");
        String expStatus = new JSONObject(response.getBody().toString()).getString("status");

        if (!"no experiment found".equals(expStatus)) {
            logger.warn("Fail to delete experiment at deterlab {}", jsonString);
            throw new ExpDeleteException();
        }

        logger.info("Delete experiment request success at deterlab", response.getBody().toString());
        return expStatus;
    }

    /**
     * Retrieves the experiment status from Deterlab
     * @implNote cannot throw exception for this method
     * @param jsonString Contains eid and pid
     * @return the status of the experiment, a "error" if the request fails
     */
    public String getExperimentStatus(String jsonString) {
        logger.info("Get experiment status - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getExpStatus(), HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            logger.warn("Adapter connection error get experiment status: {}", e);
            return "{\"status\": \"error\"}";
        }

        logger.info("Get experiment status request submitted to deterlab");

        return response.getBody().toString();
    }

    public String approveJoinRequest(String jsonString) {
        // for team leaders to accept join request
        logger.info("Approving join request to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getApproveJoinRequest(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String processJoinRequest(String jsonString) {
        JSONObject request = new JSONObject(jsonString);
        if(request.length() < 5) {
            logger.warn("NOT enough inputs to process the request. Ignored it.");
            return "{msg:process join request FAIL}";
        }
        String pid = request.getString("pid");
        String approverUid = request.getString("approverUid");
        String uid = request.getString("uid");
        String action = request.getString("action");
        String gid = request.getString("gid");
        logger.info("Processing join request to team {}, requester {}, approver {}, group {}, action {}",
                pid, uid, approverUid, gid, action);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(jsonString, headers);
        String reqUrl = (action == "approve")? properties.getApproveJoinRequest() : properties.getRejectJoinRequest();

        ResponseEntity httpResponse;
        try {
            httpResponse = restTemplate.exchange(reqUrl, HttpMethod.POST, httpRequest, String.class);
        } catch (RestClientException e) {
            logger.warn("Adapter DeterLab connection error: {}", e.getMessage());
            throw new AdapterDeterlabConnectException();
        }

        JSONObject response = new JSONObject(httpResponse.getBody().toString());
        if("process join request OK".equals(response.getString("msg"))) {
            logger.info("{} join request to team {} OK", action, pid);
        } else {
            logger.info("{} join request to team {} FAIL", action, pid);
        }
        return httpResponse.getBody().toString();
    }
    public String rejectJoinRequest(String jsonString) {
        // for team leaders to reject join request
        logger.info("Rejecting join request to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getRejectJoinRequest(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String approveProject(String jsonString) {
        // for ncl admins to approve teams
        String pid = new JSONObject(jsonString).getString("pid");
        logger.info("Approving team {} to {} at {}: {}",
                pid, properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getApproveProject(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            logger.warn("Adapter DeterLab connection error: {}", e);
            throw new AdapterDeterlabConnectException();
        }

        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        if ("approve project OK".equals(jsonResult)) {
            logger.info("Approve team {} OK", pid);
        } else {
            logger.warn("Approve team {} FAIL", pid);
        }
        return response.getBody().toString();
    }

    public String rejectProject(String jsonString) {
        // for ncl admins to reject teams
        String pid = new JSONObject(jsonString).getString("pid");
        logger.info("Rejecting team {} to {} at {}: {}",
                pid, properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity response;

        try {
            response = restTemplate.exchange(properties.getRejectProject(), HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            logger.warn("Adapter DeterLab connection error: {}", e);
            throw new AdapterDeterlabConnectException();
        }
        String jsonResult = new JSONObject(response.getBody().toString()).getString("msg");
        if ("reject project OK".equals(jsonResult)) {
            logger.info("Reject team {} OK", pid);
        } else {
            logger.warn("Reject team {} FAIL", pid);
        }
        return response.getBody().toString();
    }
}
