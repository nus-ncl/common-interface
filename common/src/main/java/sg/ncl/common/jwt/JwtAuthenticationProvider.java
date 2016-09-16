package sg.ncl.common.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sg.ncl.common.authentication.Role;

import javax.validation.constraints.NotNull;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;


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
        // TODO verify the role is valid

        try {
            final Claims claims = Jwts.parser().setSigningKey(apiKey).parseClaimsJws(token.getCredentials()).getBody();
            checkDates(claims);
            token.setDetails(claims);
            // set subject as principal
            // set granted authority
            // set authenticated to true
            token.setAuthenticated(true);

            log.info("Validation success: Principal {}", token.getPrincipal());
            log.info("Validation success: Role {}", token.getAuthorities());

//            if (isRoleValid(token.getAuthorities()) && isTokenVerified()) {
//                log.info("Jwt Token authentication SUCCESS: {}", token.getAuthorities());
//                token.setAuthenticated(true);
//            } else {
//                log.warn("Jwt Token authentication FAIL: {}", token.getAuthorities());
//                token.setAuthenticated(false);
//            }
            return token;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            log.warn("{}", e);
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    private void checkDates(Claims claims) {
        // check for expired
        // check for issued at
        // check for not before
        Date now = Date.from(ZonedDateTime.now().toInstant());
        // has expired
        if (now.after(claims.getExpiration())) {
            // throw exception
            log.warn("Claim has expired {}", claims.getExpiration());
            throw new BadCredentialsException("Claim has expired");
        }

        if (now.before(claims.getNotBefore())) {
            // cannot use it before certain date
            // throw exception
            log.warn("Claim is used before the NOT-BEFORE date {}", claims.getNotBefore());
            throw new BadCredentialsException("Claim is used before the NOT-BEFORE date");
        }

        if (now.before(claims.getIssuedAt())) {
            // throw exception
            log.warn("Claim is before issued date {}", claims.getIssuedAt());
            throw new BadCredentialsException("Claim is before issued date");
        }

        if (claims.getIssuedAt().after(claims.getExpiration())) {
            // throw exception
            log.warn("Claim is issued {} after the expiration date {}", claims.getIssuedAt(), claims.getExpiration());
            throw new BadCredentialsException("Claim is issued after the expiration date");
        }
    }

    private boolean isRoleValid(Collection<? extends GrantedAuthority> authorities) {
        // FIXME should have a better way
        SimpleGrantedAuthority user = new SimpleGrantedAuthority(Role.USER.toString());
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority(Role.ADMIN.toString());
        if (!authorities.contains(user) && !authorities.contains(admin)) {
            return false;
        }
        return true;
    }

    private boolean isTokenVerified() {
        // FIXME need to check id against database?
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }
}
