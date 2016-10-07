package sg.ncl.adapter.deterlab;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static sg.ncl.adapter.deterlab.ConnectionProperties.PREFIX;

/**
 * Created by Te Ye
 */
@ConfigurationProperties(prefix = PREFIX)
public class ConnectionProperties {

    public static final String PREFIX = "ncl.deterlab.adapter";

    private String ip;
    private String port;
    private String bossUrl;
    private String userUrl;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getJoinProjectNewUsers() {
        return "http://" + ip + ":" + port + "/joinProjectNewUsers";
    }

    public String getJoinProject() {
        return "http://" + ip + ":" + port + "/joinProject";
    }

    public String getApplyProjectNewUsers() {
        return "http://" + ip + ":" + port + "/applyProjectNewUsers";
    }

    public String getCreateExperiment() {
        return "http://" + ip + ":" + port + "/createExperiment";
    }

    public String startExperiment() {
        return "http://" + ip + ":" + port + "/startExperiment";
    }

    public String stopExperiment() {
        return "http://" + ip + ":" + port + "/stopExperiment";
    }

    public String deleteExperiment() { return "http://" + ip + ":" + port + "/deleteExperiment"; }

    public String getUpdateCredentials() {
        return "http://" + ip + ":" + port + "/changePassword";
    }

    public String getApproveJoinRequest() {
        return "http://" + ip + ":" + port + "/approveJoinRequest";
    }

    public String getRejectJoinRequest() {
        return "http://" + ip + ":" + port + "/rejectJoinRequest";
    }

    public String getApplyProject() {
        return "http://" + ip + ":" + port + "/applyProject";
    }

    public String getApproveProject() {
        return "http://" + ip + ":" + port + "/approveProject";
    }

    public String getRejectProject() {
        return "http://" + ip + ":" + port + "/rejectProject";
    }

    public String getExpStatus() {
        return "http://" + ip + ":" + port + "/getExpStatus";
    }

    public String getBossUrl() {
        return bossUrl;
    }

    public void setBossUrl(String bossUrl) {
        this.bossUrl = bossUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}

