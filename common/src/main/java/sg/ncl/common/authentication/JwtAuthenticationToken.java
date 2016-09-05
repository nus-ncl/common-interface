package sg.ncl.common.authentication;

import org.springframework.security.core.Authentication;

/**
 * @author Te Ye
 */
public class JwtAuthenticationToken implements Authentication {

    private boolean authenticated = true;
}
