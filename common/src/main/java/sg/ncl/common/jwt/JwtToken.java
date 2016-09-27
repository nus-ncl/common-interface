package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import sg.ncl.common.authentication.Role;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Te Ye, Christopher Zhong
 */
@Slf4j
public class JwtToken extends AbstractAuthenticationToken {

    public static final String KEY = "roles";

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
        super(((List<String>) claims.get(KEY, List.class)).stream().filter(Role::contains).map(Role::valueOf).collect(Collectors.toList()));
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
