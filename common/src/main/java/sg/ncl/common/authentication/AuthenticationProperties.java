package sg.ncl.common.authentication;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for configuring authentication.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = AuthenticationProperties.PREFIX)
@Getter
public class AuthenticationProperties {

    static final String PREFIX = "ncl.authentication";

    /**
     * By default, all URIs (a.k.a. endpoints) require authentication.
     * However, the map highlights the URIs that does not require authentication.
     * <p/>
     * For example, "/point"="get,post" means that the GET and POST methods for the URI "/point" does not require authentication.
     */
    private final Map<String, String> uri = new HashMap<>();

}
