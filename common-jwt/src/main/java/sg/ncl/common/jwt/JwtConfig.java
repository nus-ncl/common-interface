package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author Christopher Zhong
 */
@Configuration
@PropertySource("classpath:/sg/ncl/common/jwt/jwt.yml")
public class JwtConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    private static final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SignatureAlgorithm signatureAlgorithm(@Value("${ncl.jwt.signing-algorithm:#{null}}") final String algorithm) {
        if (algorithm == null) {
            logger.warn("No signature algorithm specified; using default: {}", DEFAULT_SIGNATURE_ALGORITHM);
            return DEFAULT_SIGNATURE_ALGORITHM;
        }
        logger.info("Looking up '{}' for signature algorithm", algorithm);
        try {
            return SignatureAlgorithm.forName(algorithm);
        } catch (SignatureException e) {
            logger.warn("Unknown signature algorithm '{}'; using default: {}", algorithm, DEFAULT_SIGNATURE_ALGORITHM);
            return DEFAULT_SIGNATURE_ALGORITHM;
        }
    }

    @Bean
    public Key key(@Value("${ncl.jwt.apiKey}") final String apiKey, final SignatureAlgorithm signatureAlgorithm) {
        logger.info("Using '{}' as the api key and signing with {}", apiKey, signatureAlgorithm);
        return new SecretKeySpec(apiKey.getBytes(), signatureAlgorithm.getJcaName());
    }

}
