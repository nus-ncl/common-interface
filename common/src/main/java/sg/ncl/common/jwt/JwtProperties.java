package sg.ncl.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Base class for configuration of JWTs.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = JwtProperties.PREFIX)
@Getter
@Setter
public class JwtProperties {

    static final String PREFIX = "ncl.jwt";

    /**
     * The algorithm to be used to sign JWTs.
     */
    private String signingAlgorithm = null;
    /**
     * The key to be used to sign JWTs.
     */
    private String apiKey = null;
    /**
     * The expiry duration of JWTs.
     */
    private String expiryDuration = null;

}
