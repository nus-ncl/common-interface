package sg.ncl.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
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
        RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) authentication;
        log.info("RememberMeAuthenticationToken token: {}", token);
        log.info("Jwt Token Name: {}", token.getName());
        log.info("Jwt Token Authorities: {}", token.getAuthorities());
        log.info("Jwt Token Credentials: {}", token.getCredentials());
        log.info("Jwt Token Details: {}", token.getDetails());
        log.info("Jwt Token Principal: {}", token.getPrincipal());
        // TODO verify the role is valid

        if (isRoleValid(token.getAuthorities()) && isTokenVerified()) {
            log.info("Jwt Token authentication SUCCESS: {}", token.getAuthorities());
            token.setAuthenticated(true);
        } else {
            log.warn("Jwt Token authentication FAIL: {}", token.getAuthorities());
            token.setAuthenticated(false);
        }
        return token;
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
        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
