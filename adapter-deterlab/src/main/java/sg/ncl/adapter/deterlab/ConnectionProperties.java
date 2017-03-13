package sg.ncl.adapter.deterlab;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

import static sg.ncl.adapter.deterlab.ConnectionProperties.PREFIX;

/**
 * Created by Te Ye
 */
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class ConnectionProperties {

    private static final String HTTP_MODE = "http://";
    public static final String PREFIX = "ncl.deterlab.adapter";

    private String ip;
    private String port;
    private String bossUrl;
    private String userUrl;
    private boolean enabled = false;

    public String login() {
        return HTTP_MODE + ip + ":" + port + "/login";
    }

    public String getJoinProjectNewUsers() {
        return HTTP_MODE + ip + ":" + port + "/joinProjectNewUsers";
    }

    public String getJoinProject() {
        return HTTP_MODE + ip + ":" + port + "/joinProject";
    }

    public String getApplyProjectNewUsers() {
        return HTTP_MODE + ip + ":" + port + "/applyProjectNewUsers";
    }

    public String getCreateExperiment() {
        return HTTP_MODE + ip + ":" + port + "/createExperiment";
    }

    public String startExperiment() {
        return HTTP_MODE + ip + ":" + port + "/startExperiment";
    }

    public String stopExperiment() {
        return HTTP_MODE + ip + ":" + port + "/stopExperiment";
    }

    public String deleteExperiment() { return HTTP_MODE + ip + ":" + port + "/deleteExperiment"; }

    public String getUpdateCredentials() {
        return HTTP_MODE + ip + ":" + port + "/changePassword";
    }

    public String getApproveJoinRequest() {
        return HTTP_MODE + ip + ":" + port + "/approveJoinRequest";
    }

    public String getRejectJoinRequest() {
        return HTTP_MODE + ip + ":" + port + "/rejectJoinRequest";
    }

    public String getApplyProject() {
        return HTTP_MODE + ip + ":" + port + "/applyProject";
    }

    public String getApproveProject() {
        return HTTP_MODE + ip + ":" + port + "/approveProject";
    }

    public String getRejectProject() {
        return HTTP_MODE + ip + ":" + port + "/rejectProject";
    }

    public String getExpStatus() {
        return HTTP_MODE + ip + ":" + port + "/getExpStatus";
    }

    public String getFreeNodes() {
        return HTTP_MODE + ip + ":" + port + "/getFreeNodes";
    }

    public String getLoggedInUsersCount() {
        return HTTP_MODE + ip + ":" + port + "/getLoggedInUsersCount";
    }

    public String getRunningExperimentsCount() {
        return HTTP_MODE + ip + ":" + port + "/getRunningExperimentsCount";
    }

    public String getTotalNodes() {
        return HTTP_MODE + ip + ":" + port + "/getTotalNodes";
    }

    public String getTopoThumbnail() {
        return HTTP_MODE + ip + ":" + port + "/getTopoThumbnail";
    }

    public String getUsageStatistics() {
        return HTTP_MODE + ip + ":" + port + "/getUsageStatistics";
    }

    public String getResetPasswordURI() {
        return HTTP_MODE + ip + ":" + port + "/resetPassword";
    }

    public String getGlobalImages() {
        return HTTP_MODE + ip + ":" + port + "/getCveImages";
    }

    public String getSavedImages() {
        return HTTP_MODE + ip + ":" + port + "/getSavedImages";
    }

    public String saveImage() {
        return HTTP_MODE + ip + ":" + port + "/saveImage";
    }

    public String removeUserFromTeam() {
        return HTTP_MODE + ip + ":" + port + "/removeUserFromTeam";
    }

}
