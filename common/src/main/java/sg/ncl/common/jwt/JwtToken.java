package sg.ncl.common.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author Te Ye
 */
public class JwtToken extends AbstractAuthenticationToken {

    private final String token;

    public JwtToken(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
