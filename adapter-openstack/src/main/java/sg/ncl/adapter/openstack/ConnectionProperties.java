package sg.ncl.adapter.openstack;

/**
 * Author: Tran Ly Vu
 */
public class ConnectionProperties {
    private static final String HTTP_MODE = "http://";
    public static final String PREFIX = "ncl.openstack.adapter";
    private String ip;
    private String port;
    private String role_id;

    public String requestTokenUrl() {
        return HTTP_MODE + ip + ":" + port + "/v3/auth/tokens";
    }

    public String createUserUrl() {
        return HTTP_MODE + ip + ":" + port + "/v3/users";
    }

    public String createProjectUrl() {
        return HTTP_MODE + ip + ":" + port + "/v3/projects";
    }

    public String addUserToProjectUrl(String project_id, String user_id) {
        return HTTP_MODE + ip + ":" + port + "/v3/projects" + project_id + "/users/"
                + user_id + "/roles" + role_id;
    }
}
