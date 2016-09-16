package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Te Ye
 */
public class JwtToken extends AbstractAuthenticationToken {

//    private final Collection<GrantedAuthority> authorities;
//    private String principal;
    private final String token;

    public JwtToken(String token) {
        super(null);
        this.token = token;
    }

//    public void setPrincipal(String principal) {
//        this.principal = principal;
//    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        if (this.getDetails() instanceof Claims) {
            return ((Claims) this.getDetails()).getSubject();
        }
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (this.getDetails() instanceof Claims) {
            return (Collection<GrantedAuthority>) ((Claims) this.getDetails()).get("roles");
        }
        return null;
    }
}
