package sg.ncl.service.adapter;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import sg.ncl.service.adapter.data.jpa.DeterlabUserRepository;
import sg.ncl.service.adapter.domain.DeterlabUser;
import sg.ncl.service.adapter.dtos.entities.DeterlabUserEntity;

import javax.inject.Inject;


/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 */
public class AdapterDeterlab {

    private DeterlabUserRepository deterlabUserRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private ConnectionProperties properties = new ConnectionProperties();

    @Inject
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
