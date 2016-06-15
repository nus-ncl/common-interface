package sg.ncl.service.adapter;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 */
public class AdapterDeterlab {

    private RestTemplate restTemplate = new RestTemplate();
    private ConnectionProperties properties = new ConnectionProperties();

    public String addUsers(String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getADD_USERS_URI(), HttpMethod.POST, request, String.class);

        // Will return the following:
        // no user created
        // user is created
        // user not found
        return responseEntity.getBody().toString();
    }
}
