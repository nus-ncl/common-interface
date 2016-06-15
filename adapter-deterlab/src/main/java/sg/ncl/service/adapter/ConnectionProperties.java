package sg.ncl.service.adapter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Te Ye
 */
@ConfigurationProperties(prefix = ConnectionProperties.PREFIX)
public class ConnectionProperties {

    public static final String PREFIX = "connection.properties";

    private String ADAPTER_IP = "172.18.178.10";
    private String ADAPTER_PORT = "8080";
    private String ADD_USERS_URI = "http://" + ADAPTER_IP + ":" + ADAPTER_PORT + "/addUsers";

    public String getADD_USERS_URI() {
        return ADD_USERS_URI;
    }

    public void setADD_USERS_URI(String ADD_USERS_URI) {
        this.ADD_USERS_URI = ADD_USERS_URI;
    }
}

