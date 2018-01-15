package sg.ncl.adapter.openstack;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.openstack.exceptions.OpenStackConnectionException;
import javax.inject.Inject;


/**
 * Author: Tran Ly Vu
 */

@Slf4j
public class AdapterOpenStack {

    private RestTemplate restTemplate;
    private ConnectionProperties properties;

    @Inject
    public AdapterOpenStack(ConnectionProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public JSONObject requestToken () {
        JSONObject domainObject = new JSONObject();
        domainObject.put("name", "Default");

        JSONObject userObject = new JSONObject();
        userObject.put("name", "admin");
        userObject.put("domain", domainObject);
        userObject.put("password", "adminpass");

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
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.requestTokenUrl(), HttpMethod.POST, request, String.class);
        } catch (JSONException e) {
            log.warn("Error requesting token in OpenStack: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error requesting token in OpenStack: {}", e);
            throw new OpenStackConnectionException(e.getMessage());
        }

        JSONObject token = new JSONObject(responseEntity.getBody().toString());

        return token;
    }

    public JSONObject createUser(String name, String password) {
        JSONObject userObject = new JSONObject();
        userObject.put("enabled", true);
        userObject.put("name", name);
        userObject.put("password", password);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", userObject);

        JSONObject token = requestToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token.toString());
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.createUserUrl(), HttpMethod.POST, request, String.class);
        } catch (ResourceAccessException e) {
            log.warn("Error requesting token in OpenStack: {}", e);
            throw new OpenStackConnectionException(e.getMessage());
        } catch (JSONException e) {
            log.warn("Error creating user in OpenStack: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error creating user in OpenStack: {}", e);
            throw new OpenStackConnectionException(e.getMessage());
        }

        return new JSONObject(responseEntity.getBody().toString());
    }

    public JSONObject createProject(String name, String description) {
        JSONObject projectObject = new JSONObject();
        projectObject.put("description", description);
        projectObject.put("name", name);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("project", projectObject);

        JSONObject token = requestToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token.toString());
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.createProjectUrl(),
                                                    HttpMethod.POST, request, String.class);
        } catch (JSONException e) {
            log.warn("Error creating project in OpenStack: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error creating project in OpenStack: {}", e);
            throw new OpenStackConnectionException(e.getMessage());
        }

        return new JSONObject(responseEntity.getBody().toString());
    }

    public void addUserToProject(String projectId, String userId, JSONObject token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token.toString());
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity responseEntity;
        try {
            responseEntity = restTemplate.exchange(properties.addUserToProjectUrl(projectId, userId),
                                                    HttpMethod.PUT, request, String.class);
        } catch (JSONException e) {
            log.warn("Error adding User to Project in OpenStack: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error adding User to Project in OpenStack: {}", e);
            throw new OpenStackConnectionException(e.getMessage());
        }
    }

}
