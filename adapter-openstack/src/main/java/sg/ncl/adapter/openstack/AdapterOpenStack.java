package sg.ncl.adapter.openstack;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.openstack.exceptions.OpenStackConnectionException;
import sg.ncl.adapter.openstack.exceptions.OpenStackInternalErrorException;

import javax.inject.Inject;


/**
 * Author: Tran Ly Vu
 */
@Component
@Slf4j
public class AdapterOpenStack {

    private RestTemplate restTemplate;
    private ConnectionProperties properties;

    @Inject
    public AdapterOpenStack(ConnectionProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    private String requestToken () {

        JSONObject userObject = new JSONObject();
        userObject.put("id", "69a0564a4994458baf70b98aa638c530");
        userObject.put("password", "adminpass");

        JSONObject passwordObject = new JSONObject();
        passwordObject.put("user", userObject);

        // password array
        JSONArray passwordMethodArray = new JSONArray();
        passwordMethodArray.put("password");

        JSONObject identityObject = new JSONObject();
        identityObject.put("methods", passwordMethodArray);
        identityObject.put("password", passwordObject);

        JSONObject projectObject = new JSONObject();
        projectObject.put("id", "f9915a7644a648af8db8ee3d8b821419");

        JSONObject scopeObject = new JSONObject();
        scopeObject.put("project", projectObject);

        JSONObject authObject = new JSONObject();
        authObject.put("identity", identityObject);
        authObject.put("scope", scopeObject);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth", authObject);

        log.info("Starting to request OpenStack token");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.requestTokenUrl(), HttpMethod.POST, request, String.class);
            log.info("Succesfully requesting OpenStack");
        } catch (ResourceAccessException e) {
            log.warn("Error in requesting OpenStack token : {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in requesting OpenStack token: {}", e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in requesting OpenStack token: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error in requesting OpenStack token: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }

        HttpHeaders headers = responseEntity.getHeaders();
        String token= headers.get("X-Subject-Token").get(0);
        log.info("OpenStack token is {}", token);

        return token;
    }


    public String createUserAndRetrieveUserId(String name, String password) {
        JSONObject userObject = new JSONObject();
        userObject.put("enabled", true);
        userObject.put("name", name);
        userObject.put("password", password);
        userObject.put("email", name); // name is email

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", userObject);

        String token = requestToken();

        log.info("Starting to create OpenStack user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.createUserUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully creating new user {} for OpenStack", name);
        } catch (ResourceAccessException e) {
            log.warn("Error in creating user: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in creating user: {}", e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating user: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error in creating user: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }

        log.info("Retrieving new OpenStack user id");
        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        log.info("Openstack new user id is {}",responseObject.getJSONObject("user").getString("id"));

        return responseObject.getJSONObject("user").getString("id");
    }

    public String createProjectAndRetrieveProjectId(String name, String description) {
        JSONObject projectObject = new JSONObject();
        projectObject.put("description", description);
        projectObject.put("domain_id", "default");
        projectObject.put("enabled", true);
        projectObject.put("is_domain", false);
        projectObject.put("name", name);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("project", projectObject);

        String token = requestToken();

        log.info("Starting to create OpenStack project");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.createProjectUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully registering new project {} for OpenStack", name);
        } catch (ResourceAccessException e) {
            log.warn("Error in creating project: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in creating project: {}", e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating project: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error in creating project: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }

        log.info("Retrieving new OpenStack project id");
        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        log.info("OpenStack new project id is {}", responseObject.getJSONObject("project").getString("id"));
        return responseObject.getJSONObject("project").getString("id");
    }


    public String addUserToProject(String userId, String projectId) {

        String token = requestToken();

        log.info("Starting to add OpenStack user {} to project {}", userId, projectId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        ResponseEntity responseEntity;
        try {
            responseEntity = restTemplate.exchange(properties.addUserToProjectUrl(projectId, userId), HttpMethod.PUT, request, String.class);
            log.info("Successfully adding user {} to project {} for OpenStack", userId, projectId);
        } catch (ResourceAccessException e) {
            log.warn("Error in adding user to project: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in adding user to project: {}", e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in adding user to project: error parsing response body");
            throw e;
        } catch (RestClientException e) {
            log.warn("Error in adding user to project: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }
        return responseEntity.getStatusCode().toString();
    }

}
