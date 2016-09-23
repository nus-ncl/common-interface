package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import sg.ncl.common.authentication.Role;

import java.util.stream.Collectors;

/**
 * @author Te Ye, Christopher Zhong
 */
public class JwtToken extends AbstractAuthenticationToken {

    private final String token;
    @Getter
    private final transient Claims claims;

    public JwtToken(final String token) {
        super(null);
        this.token = token;
        claims = null;
        setAuthenticated(false);
    }

    public JwtToken(final String token, final Claims claims) {
        super(claims.get(R.KEY, Roles.class).stream().filter(Role::contains).map(Role::valueOf).collect(Collectors.toList()));
        this.token = token;
        this.claims = claims;
        setDetails(claims);
        setAuthenticated(true);
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return getClaims();
    }

}
