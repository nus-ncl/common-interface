package sg.ncl.common.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Base class for configuring authentication.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = AuthenticationProperties.PREFIX)
@Getter
@Setter
public class AuthenticationProperties {

    static final String PREFIX = "ncl.authentication";

    /**
     * The URL endpoint for the authentication.
     */
    private String url;

}
