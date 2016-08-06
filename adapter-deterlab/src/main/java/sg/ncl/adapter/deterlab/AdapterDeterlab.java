package sg.ncl.adapter.deterlab;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;
import sg.ncl.adapter.deterlab.exceptions.AdapterDeterlabConnectException;
import sg.ncl.adapter.deterlab.exceptions.CredentialsUpdateException;
import sg.ncl.adapter.deterlab.exceptions.ExpNameAlreadyExistsException;
import sg.ncl.adapter.deterlab.exceptions.NSFileParseException;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;

import javax.inject.Inject;

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
