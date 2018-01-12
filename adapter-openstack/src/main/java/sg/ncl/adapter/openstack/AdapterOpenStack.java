package sg.ncl.adapter.openstack;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.openstack.exceptions.OpenStackConnectionException;

import javax.inject.Inject;
import javax.management.openmbean.OpenDataException;


/**
 * Author: Tran Ly Vu
 */
@Slf4j
public class AdapterOpenStack {

    @Autowired
    protected RestTemplate restTemplate;

    @Inject
    protected ConnectionProperties properties;


    public void requestToken (String name, String password) {
        JSONObject domainObject = new JSONObject();
        domainObject.put("name", "Default");

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("domain", domainObject);
        userObject.put("password", password);

        JSONObject passwordObject = new JSONObject();
        userObject.put("user", userObject);

        JSONObject identityObject = new JSONObject();
        userObject.put("methods", "password");
        userObject.put("password", passwordObject);

        JSONObject authObject = new JSONObject();
        authObject.put("identity", identityObject);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth", authObject);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        ResponseEntity response;

        try {
            response = restTemplate.exchange(
                                properties.requestTokenUrl(),
                                HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.warn("Error requesting token in OpenStack: {}", e);
            throw new OpenStackConnectionException(e.getMessage());
        }

        JSONObject token = new JSONObject(response.getBody().toString());
    }


}
