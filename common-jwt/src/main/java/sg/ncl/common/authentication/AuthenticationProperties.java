package sg.ncl.common.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = AuthenticationProperties.PREFIX)
public class AuthenticationProperties {

    public static final String PREFIX = "ncl.authentication";

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

}
