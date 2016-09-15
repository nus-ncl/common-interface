package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.expression.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sg.ncl.common.authentication.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Te Ye
 */
public class JwtToken implements Authentication {

    private static final long serialVersionUID = 1L;

    private Jwt jwt;
    private Claims claims;
    private boolean authenticated;
    private final Collection<GrantedAuthority> authorities;

    public JwtToken(Jwt jwt, Claims claims) throws ParseException {
        this.jwt = jwt;
        this.claims = claims;
        this.authenticated = false;

        Object roleList = claims.get("roles");
        List<String> roles = (List) roleList;
        List<GrantedAuthority> tmp = new ArrayList<>();
        if (roles != null) {
            for (String role : roles) {
                tmp.add(new SimpleGrantedAuthority(role));
            }
        }
        this.authorities = Collections.unmodifiableList(tmp);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return claims.getSubject();
    }

    @Override
    public Object getDetails() {
        return claims.toString();
    }

    @Override
    public Object getPrincipal() {
        return claims.getSubject();
    }

    @Override
    public String getName() {
        return claims.getSubject();
    }

    public Claims getClaims() {
        return claims;
    }

    void setClaims(Claims claims) {
        this.claims = claims;
    }

    public Jwt getJwt() {
        return jwt;
    }

    void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
