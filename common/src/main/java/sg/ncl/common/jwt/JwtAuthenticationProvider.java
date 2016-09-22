package sg.ncl.common.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;


/**
 * @author Te Ye
 */
@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Key apiKey;

    JwtAuthenticationProvider(@NotNull final Key apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken token = (JwtToken) authentication;
        log.info("JwtToken token: {}", token);
        log.info("Jwt Token Name: {}", token.getName());
        log.info("Jwt Token Authorities: {}", token.getAuthorities());
        log.info("Jwt Token Credentials: {}", token.getCredentials());
        log.info("Jwt Token Details: {}", token.getDetails());
        log.info("Jwt Token Principal: {}", token.getPrincipal());

        try {
            final Claims claims = Jwts.parser().setSigningKey(apiKey).parseClaimsJws(token.getCredentials()).getBody();
            checkDates(claims);
            token.setDetails(claims);

            log.info("Validation success: Principal {}", token.getPrincipal());
            log.info("Validation success: Role {}", token.getAuthorities());

            return token;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | PrematureJwtException e) {
            // java.lang.IllegalArgumentException: JWT String argument cannot be null or empty.
            log.warn("{}", e);
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    private void checkDates(Claims claims) {
        // check for expired
        // check for issued at
        // check for not before
        log.info("Decrypted claims: {}", claims);
        Date now = Date.from(ZonedDateTime.now().toInstant());
        // has expired
        if (now.after(claims.getExpiration())) {
            // throw exception
            log.warn("Claim has expired {}", claims.getExpiration());
            throw new BadCredentialsException("Claim has expired");
        }

        if (claims.getNotBefore() != null && now.before(claims.getNotBefore())) {
            // cannot use it before certain date
            // throw exception
            log.warn("Claim is used before the NOT-BEFORE date {}", claims.getNotBefore());
            throw new BadCredentialsException("Claim is used before the NOT-BEFORE date");
        }

        if (now.before(claims.getIssuedAt())) {
            // throw exception
            log.warn("Claim is before issued date {}", claims.getIssuedAt());
            throw new BadCredentialsException("Claim is before issued date " + claims.getIssuedAt());
        }

//        if (claims.getIssuedAt().after(claims.getExpiration())) {
//            // throw exception
//            log.warn("Claim is issued {} after the expiration date {}", claims.getIssuedAt(), claims.getExpiration());
//            throw new BadCredentialsException("Claim is issued after the expiration date");
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }
}
