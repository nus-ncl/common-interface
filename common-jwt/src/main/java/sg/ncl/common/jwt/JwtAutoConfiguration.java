package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.Key;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Configuration
@ConditionalOnClass({BCryptPasswordEncoder.class, SignatureAlgorithm.class, Key.class, Duration.class})
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JwtAutoConfiguration.class);

    private static final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final Duration DEFAULT_EXPIRY_DURATION = Duration.ofHours(24L);
    private static final UUID DEFAULT_API_KEY = UUID.randomUUID();

    @Inject
    private JwtProperties properties;

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(SignatureAlgorithm.class)
    public SignatureAlgorithm signatureAlgorithm() {
        final String value = properties.getSignatureAlgorithm();
        if (value == null || value.isEmpty()) {
            logger.warn("No signature algorithm was specified; using default: {}", DEFAULT_SIGNATURE_ALGORITHM);
            return DEFAULT_SIGNATURE_ALGORITHM;
        }
        try {
            final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(value);
            logger.info("Using specified signature algorithm: {}", signatureAlgorithm);
            return signatureAlgorithm;
        } catch (SignatureException e) {
            logger.warn("Signature algorithm '{}' is unknown; using default: {}", value, DEFAULT_SIGNATURE_ALGORITHM);
            return DEFAULT_SIGNATURE_ALGORITHM;
        }
    }

    @Bean
    @ConditionalOnMissingBean(Key.class)
    public Key apiKey(final SignatureAlgorithm signatureAlgorithm) {
        final String value = properties.getApiKey();
        if (value == null || value.isEmpty()) {
            logger.warn("No api key was specified; using default: {}", DEFAULT_API_KEY);
            return new SecretKeySpec(DEFAULT_API_KEY.toString().getBytes(), signatureAlgorithm.getJcaName());
        }
        logger.info("Using specified api key: {}", value);
        return new SecretKeySpec(value.getBytes(), signatureAlgorithm.getJcaName());
    }

    @Bean
    @ConditionalOnMissingBean(Duration.class)
    public Duration expiryDuration() {
        final String value = properties.getExpiryDuration();
        if (value == null || value.isEmpty()) {
            logger.warn("No expiry duration was specified; using default: {}", DEFAULT_EXPIRY_DURATION);
            return DEFAULT_EXPIRY_DURATION;
        }
        try {
            final Duration duration = Duration.parse(value);
            logger.info("Using specified expiry duration: {}", duration);
            return duration;
        } catch (DateTimeParseException e) {
            logger.warn("Duration format '{}' is invalid; using default: {}", value, DEFAULT_EXPIRY_DURATION);
            return DEFAULT_EXPIRY_DURATION;
        }
    }

}
