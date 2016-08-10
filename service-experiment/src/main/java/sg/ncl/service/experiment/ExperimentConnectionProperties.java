package sg.ncl.service.experiment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Desmond.
 */
@Component
@ConfigurationProperties(prefix = "deterurl")
// FIXME: this should be in the "adapter-deterlab" project
public class ExperimentConnectionProperties {
    private String bossurl;
    private String userurl;
    private String port;

    public String getBossurl() {
        return bossurl;
    }

    public void setBossurl(String bossurl) {
        this.bossurl = bossurl;
    }

    public String getUserurl() {
        return userurl;
    }

    public void setUserurl(String userurl) {
        this.userurl = userurl;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
