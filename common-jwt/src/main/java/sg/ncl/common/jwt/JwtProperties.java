package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.UUID;

import static sg.ncl.common.jwt.JwtProperties.PREFIX;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@ConfigurationProperties(prefix = PREFIX)
public class JwtProperties {

    public static final String PREFIX = "ncl.jwt";

    private String apiKey = UUID.randomUUID().toString();
    private String signatureAlgorithm = SignatureAlgorithm.HS512.name();
    private String expiryDuration = Duration.ofHours(24L).toString();

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(final String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getExpiryDuration() {
        return expiryDuration;
    }

    public void setExpiryDuration(final String expiryDuration) {
        this.expiryDuration = expiryDuration;
    }

}
