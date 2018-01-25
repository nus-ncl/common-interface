package sg.ncl.adapter.openstack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static sg.ncl.adapter.openstack.OpenStackConnectionProperties.PREFIX;

/**
 * Author: Tran Ly Vu
 */
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class OpenStackConnectionProperties {
    private static final String HTTP_MODE = "http://";
    public static final String PREFIX = "ncl.openstack.adapter";
    private static final String PROJECT_ENDPOINT = "/v3/projects/";

    private String ip;
    private String identityPort;
    private String userRoleId;

    public String requestTokenUrl() {
        return HTTP_MODE + ip + ":" + identityPort + "/v3/auth/tokens";
    }

    public String createUserUrl() {
        return HTTP_MODE + ip + ":" + identityPort + "/v3/users";
    }

    public String updateUserUrl(String userId) {
        return HTTP_MODE + ip + ":" + identityPort + "/v3/users/" + userId;
    }

    public String listUserUrl() {
        return HTTP_MODE + ip + ":" + identityPort + "/v3/users";
    }

    public String createProjectUrl() {
        return HTTP_MODE + ip + ":" + identityPort + "/v3/projects";
    }

    public String updateProjectUrl(String projectId) {
        return HTTP_MODE + ip + ":" + identityPort + PROJECT_ENDPOINT + projectId;
    }


    public String listProjectUrl() {
        return HTTP_MODE + ip + ":" + identityPort + "/v3/projects";
    }

    public String deleteProject(String projectId) {
        return HTTP_MODE + ip + ":" + identityPort + PROJECT_ENDPOINT + projectId;
    }

    public String addUserToProjectUrl(String userId, String projectId) {
        return HTTP_MODE + ip + ":" + identityPort + PROJECT_ENDPOINT + projectId + "/users/"
                + userId + "/roles/" + userRoleId;
    }
}
