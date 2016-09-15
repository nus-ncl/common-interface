package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


/**
 * @author Te Ye
 */
@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

//    private JwtSignatureValidator validator;

//    public JWTAuthenticationProvider(@NotNull final SignatureAlgorithm signatureAlgorithm, @NotNull final Key apiKey) {
//        this.validator = new DefaultJwtSignatureValidator(signatureAlgorithm, apiKey);
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authentication;
        Jwt jwt = jwtToken.getJwt();
        Claims claims = jwtToken.getClaims();
        log.info("Jwt token: {}", jwt);
        log.info("Claims: {}", claims);
        log.info("Subject: {}", claims.getSubject());
        log.info("Issued At: {}", claims.getIssuedAt());
        log.info("Expiration: {}", claims.getExpiration());
        log.info("JWTToken Name: {}", jwtToken.getName());
        log.info("JWTToken Authorities: {}", jwtToken.getAuthorities());
        log.info("JWTToken Credentials: {}", jwtToken.getCredentials());
        log.info("JWTToken Details: {}", jwtToken.getDetails());
        log.info("JWTToken Principal: {}", jwtToken.getPrincipal());
        jwtToken.setAuthenticated(true);
        return jwtToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }
}
