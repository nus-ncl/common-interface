package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Named;
import java.security.Key;
import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * @author Christopher Zhong
 */
@Configuration
@PropertySource("classpath:/sg/ncl/common/jwt/jwt.properties")
public class JwtConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    private static final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final Duration DEFAULT_EXPIRY_DURATION = Duration.ofDays(1L);

    @Named
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Named
    public SignatureAlgorithm signatureAlgorithm(@Value("${ncl.jwt.signing-algorithm}") final String algorithm) {
        if (algorithm == null) {
            logger.warn("No signature algorithm specified; using default: {}", DEFAULT_SIGNATURE_ALGORITHM);
            return DEFAULT_SIGNATURE_ALGORITHM;
        }
        try {
            final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(algorithm);
            logger.info("Signature algorithm is '{}'", signatureAlgorithm);
            return signatureAlgorithm;
        } catch (SignatureException e) {
            logger.warn("Unknown signature algorithm '{}'; using default: {}", algorithm, DEFAULT_SIGNATURE_ALGORITHM);
            return DEFAULT_SIGNATURE_ALGORITHM;
        }
    }

    @Named
    public Key key(@Value("${ncl.jwt.apiKey}") final String apiKey, final SignatureAlgorithm signatureAlgorithm) {
        logger.info("Using '{}' as the api key and signing with '{}'", apiKey, signatureAlgorithm);
        return new SecretKeySpec(apiKey.getBytes(), signatureAlgorithm.getJcaName());
    }

    @Named("ncl.jwt.expiry.duration")
    public Duration jwtExpiryDuration(@Value("${ncl.jwt.expiry.duration}") final String text) {
        try {
            final Duration duration = Duration.parse(text);
            logger.info("JWT expiry duration is '{}'", duration);
            return duration;
        } catch (DateTimeParseException e) {
            logger.warn("Invalid duration format '{}'; using default: {}", text, DEFAULT_EXPIRY_DURATION);
            return DEFAULT_EXPIRY_DURATION;
        }
    }

}
