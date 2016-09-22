package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import sg.ncl.common.authentication.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Te Ye
 */
public class JwtToken extends AbstractAuthenticationToken {

//    private final Collection<GrantedAuthority> authorities;
//    private String principal;
    private final String token;
    private Claims claims;

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
        if (this.getClaims() != null) {
            return getClaims();
        }
        return null;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        setAuthenticated(true);
        if (details instanceof Claims) {
            claims = (Claims) details;
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (this.getClaims() != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            for (Object o : getClaims().get("roles", List.class)) {
                Role r = Role.valueOf(o.toString());
                roles.add(r);
            }
//            return getClaims().get("roles", List.class).stream().map(o-> Role.valueOf(o.toString())).collect(Collectors.toList());
            return roles;
        }
        return null;
    }

    private Claims getClaims() {
        return claims;
    }
}
