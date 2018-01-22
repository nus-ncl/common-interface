package sg.ncl.adapter.openstack;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
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

        JSONObject parameters = new JSONObject();
        parameters.put("auth", authObject);

        log.info("Starting to request OpenStack token");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.requestTokenUrl(), HttpMethod.POST, request, String.class);
            log.info("Succesfully requesting OpenStack token");
        } catch (ResourceAccessException e) {
            log.warn("Error in requesting OpenStack token : {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in requesting OpenStack token: {}", e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in requesting OpenStack token: error parsing response body");
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn("Error in requesting OpenStack token: {}", e.getMessage());
            throw e;
        }

        HttpHeaders headers = responseEntity.getHeaders();
        String token= headers.get("X-Subject-Token").get(0);
        log.info("OpenStack token is {}", token);

        return token;
    }


    public void createOpenStackUser(String userName, String password) {
        JSONObject userObject = new JSONObject();
        userObject.put("enabled", true);
        userObject.put("name", userName);
        userObject.put("password", password);
        userObject.put("email", userName); // name is email

        JSONObject parameters = new JSONObject();
        parameters.put("user", userObject);

        String token = requestToken();

        log.info("Starting to create OpenStack user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.createUserUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully creating new OpenStack user {}", userName);
        } catch (ResourceAccessException e) {
            log.warn("Error in creating new OpenStack user {}: {}", userName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in creating new OpenStack user {}: {}", userName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating new OpenStack user {}: error parsing response body", userName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn("Error in creating new OpenStack user {}: {}", userName, e.getMessage());
            throw e;
        }
    }



    public void createOpenStackProject(String projectName, String description) {
        JSONObject projectObject = new JSONObject();
        projectObject.put("description", description);
        projectObject.put("domain_id", "default");
        projectObject.put("enabled", true);
        projectObject.put("is_domain", false);
        projectObject.put("name", projectName);

        JSONObject parameters = new JSONObject();
        parameters.put("project", projectObject);

        String token = requestToken();

        log.info("Starting to create OpenStack project");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);

        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.createProjectUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully creating new OpenStack project {}", projectName);
        } catch (ResourceAccessException e) {
            log.warn("Error in creating new OpenStack project {}: {}", projectName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in creating new OpenStack project {}: {}",projectName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating new OpenStack project {}: error parsing response body", projectName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn("Error in creating new OpenStack project {}: {}", projectName, e.getMessage());
            throw e;
        }
    }


    public void addUserToProject(String openStackUserId, String openStackProjectId) {

        String token = requestToken();

        log.info("Starting to add OpenStack user {} to project {}", openStackUserId, openStackProjectId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        try {
            restTemplate.exchange(properties.addUserToProjectUrl(openStackUserId, openStackProjectId), HttpMethod.PUT, request, String.class);
            log.info("Successfully adding OpenStack user {} to project {}", openStackUserId, openStackProjectId);
        } catch (ResourceAccessException e) {
            log.warn("Error in adding OpenStack user to project: {}", e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error in adding OpenStack user to project: {}", e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in adding OpenStack user to project: error parsing response body");
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn("Error in adding OpenStack user to project: {}", e.getMessage());
            throw e;
        }
    }

    public String retrieveOpenStackUserId(String userName) {
        String token = requestToken();

        JSONObject parameters =  new JSONObject();
        parameters.put("enabled", "true");
        parameters.put("name", userName);

        log.info("Starting to retrieve OpenStack user id from user name {}", userName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listUserUrl(true, userName), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack user id from user name {}", userName);
        } catch (ResourceAccessException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}", userName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }  catch (JSONException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}: error parsing response body", userName);
            throw e;
        } catch (HttpServerErrorException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}", userName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (HttpClientErrorException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}", userName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        String userId = responseObject.getJSONArray("users").getJSONObject(0).getString("id");
        log.info("OpenStack user id of user name {} is {}", userName, userId);

        return userId;
    }

    public String retrieveOpenStackProjectId(String projectName) {
        String token = requestToken();

        JSONObject parameters =  new JSONObject();
        parameters.put("enabled", "true");
        parameters.put("name", projectName);

        log.info("Starting to retrieve OpenStack project id from project name {}", projectName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listProjectUrl(true, projectName), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack project id from project name {}", projectName);
        } catch (ResourceAccessException e) {
            log.warn("Error in retrieving OpenStack project id from project name {}", projectName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Error retrieving OpenStack project id from project name {}", projectName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in retrieving OpenStack project id from project name {}: error parsing response body", projectName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn("Error in retrieving OpenStack project id from project name {}", projectName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        String projectId = responseObject.getJSONArray("projects").getJSONObject(0).getString("id");
        log.info("OpenStack project id of project name {} is {}", projectName, projectId);

        return projectId;
    }

    public void deleteOpenStackProject(String projectId) {
        String token = requestToken();

        log.info("Starting to delete OpenStack project id {}", projectId);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Auth-Token", token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        try {
            restTemplate.exchange(properties.deleteProject(projectId), HttpMethod.DELETE, request, String.class);
            log.info("Successfully deleting OpenStack project id {}", projectId);
        } catch (ResourceAccessException e) {
            log.warn("Error in deleting OpenStack project id {}: {}", projectId, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (JSONException e) {
            log.warn("Error in deleting OpenStack project id {}: error parsing response body", projectId);
            throw e;
        }  catch (HttpServerErrorException e) {
            log.warn("Error when deleting OpenStack project id {}: {}", projectId, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (HttpClientErrorException e) {
            log.warn("Error when deleting OpenStack project id {}: {}", projectId, e.getMessage());
            throw e;
        }
    }

}
;