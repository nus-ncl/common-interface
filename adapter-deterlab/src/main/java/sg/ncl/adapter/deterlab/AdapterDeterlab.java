package sg.ncl.adapter.deterlab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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
    private RestTemplate restTemplate = new RestTemplate();
    private ConnectionProperties properties = new ConnectionProperties();

    public AdapterDeterlab() {

    }

    @Autowired
    public AdapterDeterlab(DeterlabUserRepository repository) {
        this.deterlabUserRepository = repository;
    }

    public String addUsers(String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getADD_USERS_URI(), HttpMethod.POST, request, String.class);

        // Will return the following JSON:
        // msg: no user created, uid: xxx
        // msg: user is created, uid: xxx
        // msg: user not found, uid: xxx
//        System.out.println(responseEntity.getBody().toString());
        return responseEntity.getBody().toString();
    }

    public void saveDeterUserIdMapping(String deterUserId, String nclUserId) {
        DeterlabUserEntity deterlabUserEntity = new DeterlabUserEntity();
        deterlabUserEntity.setNclUserId(nclUserId);
        deterlabUserEntity.setDeterUserId(deterUserId);
        deterlabUserRepository.save(deterlabUserEntity);
    }
}
