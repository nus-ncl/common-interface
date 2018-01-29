package sg.ncl.adapter.openstack;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import sg.ncl.adapter.openstack.exceptions.*;

import javax.inject.Inject;

/**
 * Author: Tran Ly Vu
 */
@Component
@Slf4j
public class AdapterOpenStack {

    private RestTemplate restTemplate;
    private OpenStackConnectionProperties properties;

    private static final String ERROR_REQUEST_TOKEN = "Error in requesting OpenStack token: {}";
    private static final String ERROR_CREATE_USER = "Error in creating new OpenStack user {}: {}";
    private static final String ERROR_CREATE_PROJECT = "Error in creating new OpenStack project {}: {}";
    private static final String ERROR_ENABLE_USER = "Error in enabling OpenStack user id {}: {}";
    private static final String ERROR_ENABLE_PROJECT = "Error in enabling OpenStack project id {}: {}";
    private static final String ERROR_ADD_USER_TO_PROJECT = "Error in adding OpenStack user to project: {}";
    private static final String ERROR_RETRIEVE_USER_ID = "Error in retrieving OpenStack user id from user name {}";
    private static final String ERROR_RETRIEVE_PROJECT_ID = "Error in retrieving OpenStack project id from project name {}";
    private static final String ERROR_CHECK_IF_USER_EXIST = "Error in checking if user name {} already exists";
    private static final String ERROR_CHECK_IF_PROJECT_EXIST = "Error in checking if project name {} already exists";
    private static final String ERROR_DELETE_PROJECT_ID = "Error in deleting OpenStack project id {}: {}";
    private static final String PASS_KEY  = "password";
    private static final String ENABLED_KEY = "enabled";
    private static final String PROJECT_KEY = "project";
    private static final String PROJECTS_KEY = "projects";
    private static final String USERS_KEY = "users";
    private static final String X_AUTH_TOKEN_KEY = "X-Auth-Token";


    @Inject
    public AdapterOpenStack(OpenStackConnectionProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        //somehow spring does not work with PATCH => the following solution is found on stackoverflow
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
    }

    private String requestToken () {

        JSONObject userObject = new JSONObject();
        userObject.put("id", properties.getAdminId());
        userObject.put(PASS_KEY, properties.getAdminPass());

        JSONObject passwordObject = new JSONObject();
        passwordObject.put("user", userObject);

        // password array
        JSONArray passwordMethodArray = new JSONArray();
        passwordMethodArray.put(PASS_KEY);

        JSONObject identityObject = new JSONObject();
        identityObject.put("methods", passwordMethodArray);
        identityObject.put(PASS_KEY, passwordObject);

        JSONObject projectObject = new JSONObject();
        projectObject.put("id", properties.getAdminProjectId());

        JSONObject scopeObject = new JSONObject();
        scopeObject.put(PROJECT_KEY, projectObject);

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
            log.warn(ERROR_REQUEST_TOKEN, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_REQUEST_TOKEN, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in requesting OpenStack token: error parsing response body");
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_REQUEST_TOKEN, e.getMessage());
            throw e;
        }

        HttpHeaders headers = responseEntity.getHeaders();
        return headers.get("X-Subject-Token").get(0);
    }


    public void createOpenStackUser(String userName, String password) {
        JSONObject userObject = new JSONObject();
        userObject.put(ENABLED_KEY, false);
        userObject.put("name", userName);
        userObject.put(PASS_KEY, password);
        userObject.put("email", userName); // name is email

        JSONObject parameters = new JSONObject();
        parameters.put("user", userObject);

        log.info("Request OpenStack token to create OpenStack user");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to create OpenStack user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
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


    public void createOpenStackProject(String projectName, String description) {
        JSONObject projectObject = new JSONObject();
        projectObject.put("description", description);
        projectObject.put("domain_id", "default");
        projectObject.put(ENABLED_KEY, false);
        projectObject.put("is_domain", false);
        projectObject.put("name", projectName);

        JSONObject parameters = new JSONObject();
        parameters.put(PROJECT_KEY, projectObject);

        log.info("Request OpenStack token to create OpenStack project");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to create OpenStack project");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);

        try {
            restTemplate.exchange(properties.createProjectUrl(), HttpMethod.POST, request, String.class);
            log.info("Successfully creating new OpenStack project {}", projectName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_CREATE_PROJECT, projectName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_CREATE_PROJECT,projectName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in creating new OpenStack project {}: error parsing response body", projectName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_CREATE_PROJECT, projectName, e.getMessage());
            throw e;
        }
    }


    public void enableOpenStackUser(String userId) {
        JSONObject userObject = new JSONObject();
        userObject.put(ENABLED_KEY, true);

        JSONObject parameters = new JSONObject();
        parameters.put("user", userObject);

        log.info("Request OpenStack token to enable OpenStack user");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to enable OpenStack user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
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


    public void enableOpenStackProject(String projectId) {
        JSONObject projectObject = new JSONObject();
        projectObject.put(ENABLED_KEY, true);

        JSONObject parameters = new JSONObject();
        parameters.put(PROJECT_KEY, projectObject);

        log.info("Request OpenStack token to enable OpenStack project");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to enable OpenStack project");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
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
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
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

    public String retrieveOpenStackUserId(String userName) {
        JSONObject parameters =  new JSONObject();
        parameters.put("name", userName);

        log.info("Request OpenStack token to retrieve OpenStack user id");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to retrieve OpenStack user id from user name {}", userName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listUserUrl(userName), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack user id from user name {}", userName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_RETRIEVE_USER_ID, userName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        }  catch (JSONException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}: error parsing response body", userName);
            throw e;
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_RETRIEVE_USER_ID, userName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_RETRIEVE_USER_ID, userName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        try {
            JSONArray userArray = responseObject.getJSONArray(USERS_KEY);
            if (userArray.length() == 1) {
                log.warn(ERROR_RETRIEVE_USER_ID, userName);
                throw new OpenStackUserNotFoundException("OpenStack user " + userName + " not found");
            } else if (userArray.length() > 1) {
                log.warn(ERROR_RETRIEVE_USER_ID, userName);
                throw new OpenStackDuplicateUserException("More than 1 OpenStack user with same name " + userName + " found");
            } else {
                return responseObject.getJSONArray(USERS_KEY).getJSONObject(0).getString("id");
            }
        } catch (JSONException e) {
            log.warn("Error in retrieving OpenStack user id from user name {}:cant retrieve user array from OpenStack", userName);
            throw e;
        }

    }

    public String retrieveOpenStackProjectId(String projectName) {
        JSONObject parameters =  new JSONObject();
        parameters.put("name", projectName);

        log.info("Request OpenStack token to retrieve OpenStack user id");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start to retrieve OpenStack project id from project name {}", projectName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(parameters.toString(), httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listProjectUrl(projectName), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack project id from project name {}", projectName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_RETRIEVE_PROJECT_ID, projectName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_RETRIEVE_PROJECT_ID, projectName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn(ERROR_RETRIEVE_PROJECT_ID + ": error parsing response body", projectName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_RETRIEVE_PROJECT_ID, projectName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        try {
            JSONArray projectIdJsonArray = responseObject.getJSONArray(PROJECTS_KEY);
            if (projectIdJsonArray.length() == 0) {
                log.warn(ERROR_RETRIEVE_PROJECT_ID, projectName);
                throw new OpenStackProjectNotFoundException("OpenStack project " + projectName + " not found");
            }  else if (projectIdJsonArray.length() > 1) {
                log.warn(ERROR_RETRIEVE_PROJECT_ID, projectName);
                throw new OpenStackDuplicateProjectException("More than 1 OpenStack project with same name " + projectName + " found");
            } else {
                return responseObject.getJSONArray(PROJECTS_KEY).getJSONObject(0).getString("id");
            }
        } catch (JSONException e){
            log.warn(ERROR_RETRIEVE_PROJECT_ID + ": project array cant be retrieve from OpenStack", projectName);
            throw e;
        }

    }

    public void deleteOpenStackProject(String projectId) {
        log.info("Request OpenStack token to delete OpenStack project");
        String token = requestToken();

        log.info("Successful requesting OpenStack token and start to delete OpenStack project id {}", projectId);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        try {
            restTemplate.exchange(properties.deleteProject(projectId), HttpMethod.DELETE, request, String.class);
            log.info("Successfully deleting OpenStack project id {}", projectId);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_DELETE_PROJECT_ID, projectId, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (JSONException e) {
            log.warn("Error in deleting OpenStack project id {}: error parsing response body", projectId);
            throw e;
        }  catch (HttpServerErrorException e) {
            log.warn(ERROR_DELETE_PROJECT_ID, projectId, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_DELETE_PROJECT_ID, projectId, e.getMessage());
            throw e;
        }
    }

    // check if exactly 1 project name exists
    public boolean isProjectNameAlreadyExist(String projectName) {
        log.info("Request OpenStack token to check if project name already exists");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start check if project name {} already exists", projectName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listProjectUrl(projectName), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack project id from project name {} to check if project name already exists", projectName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_CHECK_IF_PROJECT_EXIST, projectName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_CHECK_IF_PROJECT_EXIST, projectName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in checking if project name {} already exists: error parsing response body", projectName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_CHECK_IF_PROJECT_EXIST, projectName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        try{
            JSONArray projectJsonArray = responseObject.getJSONArray(PROJECTS_KEY);
            if (projectJsonArray.length() == 1) { // only exactly 1 already exists
                return true;
            } else if (projectJsonArray.length() > 1) {
                log.warn(ERROR_CHECK_IF_PROJECT_EXIST, projectName);
                throw new OpenStackDuplicateProjectException("More than 1 OpenStack project with name " + projectName + " exists");
            } else {
                return false;
             }
        } catch (JSONException e) {
            log.warn("Error in checking if project name {} already exists: project array cant be retrieve from OpenStack", projectName);
            throw e;
        }
    }

    public boolean isUserNameAlreadyExist(String userName) {
        log.info("Request OpenStack token to check if project name already exists");
        String token = requestToken();

        log.info("Succesfully requesting OpenStack token and start check if user name {} already exists", userName);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(X_AUTH_TOKEN_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(properties.listUserUrl(userName), HttpMethod.GET, request, String.class);
            log.info("Successfully retrieving OpenStack user id from user name {} to check if project name already exists", userName);
        } catch (ResourceAccessException e) {
            log.warn(ERROR_CHECK_IF_USER_EXIST, userName, e.getMessage());
            throw new OpenStackConnectionException(e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn(ERROR_CHECK_IF_USER_EXIST, userName, e.getMessage());
            throw new OpenStackInternalErrorException();
        } catch (JSONException e) {
            log.warn("Error in checking if user name {} already exists: error parsing response body", userName);
            throw e;
        } catch (HttpClientErrorException e) {
            log.warn(ERROR_CHECK_IF_USER_EXIST, userName, e.getMessage());
            throw e;
        }

        JSONObject responseObject = new JSONObject(responseEntity.getBody().toString());
        try{
            JSONArray usersJsonArray = responseObject.getJSONArray(USERS_KEY);

            if (usersJsonArray.length() == 1) {
                return true;
            } else if (usersJsonArray.length() > 1) {
                log.warn(ERROR_CHECK_IF_USER_EXIST, userName);
                throw new OpenStackDuplicateUserException("There are more than 1 OpenStack user with name " + userName+ " exists");
            } else {
                return true;
            }
        } catch (JSONException e) {
            log.warn("Error in checking if user name {} already exists: user array cant be retrieve from OpenStack", userName);
            throw e;
        }

    }
}
