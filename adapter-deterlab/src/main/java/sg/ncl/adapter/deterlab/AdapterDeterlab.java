package sg.ncl.adapter.deterlab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;

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

    @Autowired
    private RestTemplate restTemplate;

    @Inject
    public AdapterDeterlab(DeterlabUserRepository repository, ConnectionProperties connectionProperties) {
        this.deterlabUserRepository = repository;
        this.properties = connectionProperties;
    }

    public String addUsers(String jsonString) {

        logger.info("Sending message to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getAddUsersUri(), HttpMethod.POST, request, String.class);

        // Will return the following JSON:
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
//        System.out.println(responseEntity.getBody().toString());
        return responseEntity.getBody().toString();
    }

    public String applyProjectNewUsers(String jsonString) {
        logger.info("Sending message to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getApplyProjectNewUsers(), HttpMethod.POST, request, String.class);

        System.out.println(responseEntity.getBody().toString());
        return responseEntity.getBody().toString();
    }

    public String updateCredentials(String jsonString) {
        logger.info("Updating credentials to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);
        ResponseEntity responseEntity = restTemplate.exchange(properties.getUpdateCredentials(), HttpMethod.POST, request, String.class);
        return responseEntity.getBody().toString();
    }

    public void saveDeterUserIdMapping(String deterUserId, String nclUserId) {
        DeterlabUserEntity deterlabUserEntity = new DeterlabUserEntity();
        deterlabUserEntity.setNclUserId(nclUserId);
        deterlabUserEntity.setDeterUserId(deterUserId);
        deterlabUserRepository.save(deterlabUserEntity);
    }

    public String createExperiment(String jsonString) {

        logger.info("Sending message to {} at {}: {}", properties.getIp(), properties.getPort(), jsonString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getCreateExperimentUri(), HttpMethod.POST, request, String.class);

        return responseEntity.getBody().toString();
    }
}
