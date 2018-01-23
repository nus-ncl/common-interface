package sg.ncl.adapter.openstack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static sg.ncl.adapter.openstack.ConnectionProperties.PREFIX;

/**
 * Author: Tran Ly Vu
 */
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class ConnectionProperties {
    private static final String HTTP_MODE = "http://";
    public static final String PREFIX = "ncl.openstack.adapter";
    private String ip;
    private String identity_port;
    private String user_role_id;

    protected String requestTokenUrl() {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/auth/tokens";
    }

    protected String createUserUrl() {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/users";
    }

    protected String updateUserUrl(String userId) {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/users/" + userId;
    }

    protected String listUserUrl(String userName) {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/users?name=" + userName;
    }

    protected String createProjectUrl() {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/projects";
    }

    protected String updateProjectUrl(String projectId) {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/users/" + projectId;
    }


    protected String listProjectUrl(String userProject) {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/projects?name=" + userProject;
    }

    protected String deleteProject(String projectId) {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/projects/" + projectId;
    }

    protected String addUserToProjectUrl(String userId, String projectId) {
        return HTTP_MODE + ip + ":" + identity_port + "/v3/projects/" + projectId + "/users/"
                + userId + "/roles/" + user_role_id;
    }
}
