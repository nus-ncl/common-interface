package sg.ncl.adapter.deterlab;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.adapter.deterlab.domain.DeterlabUser;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 */
@Component
public class AdapterDeterlab {

    private DeterlabUserRepository deterlabUserRepository;
    private ConnectionProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(AdapterDeterlab.class);

    @Inject
    private RestTemplate restTemplate;

    @Inject
    public AdapterDeterlab(DeterlabUserRepository repository, ConnectionProperties connectionProperties) {
        this.deterlabUserRepository = repository;
        this.properties = connectionProperties;
    }

    public String joinProjectNewUsers(String jsonString) {

        logger.info("Joining project as new user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getJoinProjectNewUsers(), HttpMethod.POST, request, String.class);

        // Will return the following JSON:
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
        return responseEntity.getBody().toString();
    }

    public String applyProjectNewUsers(String jsonString) {
        logger.info("Applying new project as new user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getApplyProjectNewUsers(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    // for logged on users
    public String applyProject(String jsonString) {
        logger.info("Applying project as logged on user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getApplyProject(), HttpMethod.POST, request, String.class);
        return responseEntity.getBody().toString();
    }

    // for logged on users
    public String joinProject(String jsonString) {
        logger.info("Joining project as logged on user to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getJoinProject(), HttpMethod.POST, request, String.class);
        return responseEntity.getBody().toString();
    }

    public boolean updateCredentials(String jsonString) {
        logger.info("Updating credentials to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);
        ResponseEntity respEntity = restTemplate.exchange(properties.getUpdateCredentials(), HttpMethod.POST, request, String.class);

        String jsonResult = new JSONObject(respEntity.getBody().toString()).getString("msg");
        if (jsonResult.equals("password change fail")) {
            return false;
        }

        return true;
    }

    public void saveDeterUserIdMapping(String deterUserId, String nclUserId) {
        DeterlabUserEntity deterlabUserEntity = new DeterlabUserEntity();
        deterlabUserEntity.setNclUserId(nclUserId);
        deterlabUserEntity.setDeterUserId(deterUserId);
        deterlabUserRepository.save(deterlabUserEntity);
    }

    public String getDeterUserIdByNclUserId(String nclUserId) {
        DeterlabUserEntity deterlabUserEntity = deterlabUserRepository.findByNclUserId(nclUserId);
        if (deterlabUserEntity == null) {
            throw new UserNotFoundException();
        }
        return deterlabUserEntity.getDeterUserId();
    }

    public String createExperiment(String jsonString) {
        logger.info("Sending message to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getCreateExperiment(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String startExperiment(String jsonString) {
        logger.info("Start experiment - {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.startExperiment(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String stopExperiment(String jsonString) {
        logger.info("Stop experiment - {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.stopExperiment(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String deleteExperiment(String jsonString) {
        logger.info("Delete experiment - {} at {} : {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.deleteExperiment(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String approveJoinRequest(String jsonString) {
        // for team leaders to accept join request
        logger.info("Approving join request to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getApproveJoinRequest(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String rejectJoinRequest(String jsonString) {
        // for team leaders to reject join request
        logger.info("Rejecting join request to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getRejectJoinRequest(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String approveProject(String jsonString) {
        // for ncl admins to approve teams
        logger.info("Approving team to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getApproveProject(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }

    public String rejectProject(String jsonString) {
        // for ncl admins to reject teams
        logger.info("Rejecting team to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getRejectProject(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }
}
