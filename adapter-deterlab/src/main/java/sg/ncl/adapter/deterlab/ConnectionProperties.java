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

    public String getApplyProjectNewUsers() {
        return "http://" + ip + ":" + port + "/applyProjectNewUsers";
    }

//    public void setAddUsersUri(String addUsersUri) {
//         not implemented
//    }
}

