package sg.ncl.common.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.expression.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Te Ye
 */
public class JwtAuthenticationToken implements Authentication {

    private boolean authenticated;
    private final Collection<GrantedAuthority> authorities;
    private Claims claims;

    public JwtAuthenticationToken(String authToken) throws ParseException {
        List<String> roles = new ArrayList<>();
        roles.add("roles01");
        roles.add("roles02");
        authorities = new ArrayList<>();
        for (String role: roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        this.claims = Jwts.parser().setSigningKey("myKey").parseClaimsJws(authToken).getBody();
        authenticated = false;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Claims getClaims() {
        return claims;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return claims.getSubject();
    }

    @Override
    public String getName() {
        return claims.getSubject();
    }

    @Override
    public Object getDetails() {
        return claims;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}