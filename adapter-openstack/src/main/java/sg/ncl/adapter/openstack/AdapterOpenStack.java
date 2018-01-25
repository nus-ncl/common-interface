package sg.ncl.adapter.openstack;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import sg.ncl.adapter.openstack.exceptions.OpenStackConnectionException;
import sg.ncl.adapter.openstack.exceptions.OpenStackInternalErrorException;

import javax.inject.Inject;

import static sg.ncl.adapter.openstack.OpenStackConnectionProperties.PREFIX;


/**
 * Author: Tran Ly Vu
 */
@Component
@Slf4j
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class AdapterOpenStack {
    public static final String PREFIX = "ncl.openstack.adapter";

    private RestTemplate restTemplate;
    private OpenStackConnectionProperties properties;
    private static final String ERROR_CREATE_USER = "Error in creating new OpenStack user {}: {}";
    private static final String ERROR_ENABLE_USER = "Error in enabling OpenStack user id {}: {}";
    private static final String ERROR_ADD_USER_TO_PROJECT = "Error in adding OpenStack user to project: {}";
    private static final String ERROR_ENABLE_PROJECT = "Error in enabling OpenStack project id {}: {}";
    private static final String ERROR_RETRIEVE_USER = "Error in retrieving OpenStack user id from user name {}";
    private static final String PASSWORD_KEY  = "password";
    private static final String ENABLED = "enabled";
    private static final String PROJECT = "project";
    private static final String X_AUTH_TOKEN = "X-Auth-Token";
    private  static final String ERR_CREATE_PROJECT = "Error in creating new OpenStack project {}: {}";

    private String adminPass;


    @Inject
    public AdapterOpenStack(OpenStackConnectionProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        //somehow spring does not work with PATCH => the following solution is found on stackoverflow
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
    }

    public String requestToken () {

        JSONObject userObject = new JSONObject();
        userObject.put("id", "69a0564a4994458baf70b98aa638c530");
        userObject.put(PASSWORD_KEY, adminPass);

        JSONObject passwordObject = new JSONObject();
        passwordObject.put("user", userObject);

        // password array
        JSONArray passwordMethodArray = new JSONArray();
        passwordMethodArray.put(PASSWORD_KEY);

        JSONObject identityObject = new JSONObject();
        identityObject.put("methods", passwordMethodArray);
        identityObject.put(PASSWORD_KEY, passwordObject);

        JSONObject projectObject = new JSONObject();
        projectObject.put("id", "f9915a7644a648af8db8ee3d8b821419");

        JSONObject scopeObject = new JSONObject();
        scopeObject.put(PROJECT, projectObject);

        JSONObject authObject = new JSONObject();
        authObject.put("identity", identityObject);
        authObject.put("scope", scopeObject);

        JSONObject parameters = new JSONObject();
        parameters.put("auth", authObject);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.requestTokenUrl(), HttpMethod.POST, request, String.class);
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
        return headers.get("X-Subject-Token").get(0);
    }


    public void createOpenStackUser(String userName, String password) {
        JSONObject userObject = new JSONObject();
        userObject.put(ENABLED, false);
        userObject.put("name", userName);
        userObject.put(PASSWORD_KEY, password);
        userObject.put("email", userName); // name is email

        JSONObject parameters = new JSONObject();
        parameters.put("user", userObject);

        log.info("Request OpenStack token to create OpenStack user");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to create OpenStack user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);

        try {
            restTemplate.exchange(properties.createUserUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully creating new OpenStack user {}", userName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_CREATE_USER, userName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_CREATE_USER, userName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating new OpenStack user {}: error parsing response body", userName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_CREATE_USER, userName, e.getMessage());
            throw e;
        }
    }

    public void enableOpenStackUser(String userId) {
        JSONObject userObject = new JSONObject();
        userObject.put(ENABLED, true);

        JSONObject parameters = new JSONObject();
        parameters.put("user", userObject);

        log.info("Request OpenStack token to enable OpenStack user");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to enable OpenStack user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);

        try {
            restTemplate.exchange(properties.updateUserUrl(userId), HttpMethod.PATCH, request, String.class);
            log.info("Successfully enabling OpenStack user id {}", userId);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_ENABLE_USER, userId, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_ENABLE_USER,userId, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in enabling OpenStack user id {}: error parsing response body", userId);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_ENABLE_USER, userId, e.getMessage());
            throw e;
        }
    }


    public void createOpenStackProject(String projectName, String description) {
        JSONObject projectObject = new JSONObject();
        projectObject.put("description", description);
        projectObject.put("domain_id", "default");
        projectObject.put(ENABLED, false);
        projectObject.put("is_domain", false);
        projectObject.put("name", projectName);

        JSONObject parameters = new JSONObject();
        parameters.put(PROJECT, projectObject);

        log.info("Request OpenStack token to create OpenStack project");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to create OpenStack project");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);

        try {
            restTemplate.exchange(properties.createProjectUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully creating new OpenStack project {}", projectName);
        } catch (ResourceAccessException e) {
            log.warn(ERR_CREATE_PROJECT, projectName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERR_CREATE_PROJECT,projectName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating new OpenStack project {}: error parsing response body", projectName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERR_CREATE_PROJECT, projectName, e.getMessage());
            throw e;
        }
    }

    public void enableOpenStackProject(String projectId) {
        JSONObject projectObject = new JSONObject();
        projectObject.put(ENABLED, true);

        JSONObject parameters = new JSONObject();
        parameters.put(PROJECT, projectObject);

        log.info("Request OpenStack token to enable OpenStack project");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to enable OpenStack project");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);

        try {
            restTemplate.exchange(properties.updateProjectUrl(projectId), HttpMethod.PATCH, request, String.class);
            log.info("Successfully enabling OpenStack project id {}", projectId);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_ENABLE_PROJECT, projectId, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_ENABLE_PROJECT, projectId, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in enabling OpenStack project id {}: error parsing response body", projectId);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_ENABLE_PROJECT, projectId, e.getMessage());
            throw e;
        }
    }



    public void addUserToProject(String openStackUserId, String openStackProjectId) {

        log.info("Request OpenStack token to add OpenStack user to project");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and starting to add OpenStack user {} to project {}", openStackUserId, openStackProjectId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        try {
            restTemplate.exchange(properties.addUserToProjectUrl(openStackUserId, openStackProjectId), HttpMethod.PUT, request, String.class);
            log.info("Successfully adding OpenStack user {} to project {}", openStackUserId, openStackProjectId);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_ADD_USER_TO_PROJECT, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_ADD_USER_TO_PROJECT, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in adding OpenStack user to project: error parsing response body");
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_ADD_USER_TO_PROJECT, e.getMessage());
            throw e;
        }
    }

    public String retrieveOpenStackUserId(String userName, boolean isEnabled) {
        JSONObject parameters =  new JSONObject();
        parameters.put(ENABLED, isEnabled); //for stronger filter
        parameters.put("name", userName);

        log.info("Request OpenStack token to retrieve OpenStack user id");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to retrieve OpenStack user id from user name {}", userName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listUserUrl(), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack user id from user name {}", userName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_RETRIEVE_USER, userName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }  catch (JSONException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}: error parsing response body", userName);
            throw e;
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_RETRIEVE_USER, userName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_RETRIEVE_USER, userName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        return responseObject.getJSONArray("users").getJSONObject(0).getString("id");
    }

    public String retrieveOpenStackProjectId(String projectName, boolean isEnabled) {
        JSONObject parameters =  new JSONObject();
        parameters.put(ENABLED, isEnabled);  // for stronger filter
        parameters.put("name", projectName);

        log.info("Request OpenStack token to retrieve OpenStack user id");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and to retrieve OpenStack project id from project name {}", projectName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listProjectUrl(), HttpMethod.GET, request, String.class);
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
        return responseObject.getJSONArray("projects").getJSONObject(0).getString("id");
    }

    public void deleteOpenStackProject(String projectId) {
        log.info("Request OpenStack token to delete OpenStack project");
        String token = requestToken();

        log.info("Successful requesting Openstack token and start to delete OpenStack project id {}", projectId);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN, token);
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
