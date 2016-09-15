package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sg.ncl.common.authentication.Role;

import java.util.Collection;


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
        log.info("Jwt Token Name: {}", jwtToken.getName());
        log.info("Jwt Token Authorities: {}", jwtToken.getAuthorities());
        log.info("Jwt Token Credentials: {}", jwtToken.getCredentials());
        log.info("Jwt Token Details: {}", jwtToken.getDetails());
        log.info("Jwt Token Principal: {}", jwtToken.getPrincipal());
        // TODO verify the role is valid

        if (isRoleValid(jwtToken.getAuthorities()) && isTokenVerified()) {
            log.info("Jwt Token authentication SUCCESS: {}", jwtToken.getAuthorities());
            jwtToken.setAuthenticated(true);
        } else {
            log.warn("Jwt Token authentication FAIL: {}", jwtToken.getAuthorities());
            jwtToken.setAuthenticated(false);
        }
        return jwtToken;
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
