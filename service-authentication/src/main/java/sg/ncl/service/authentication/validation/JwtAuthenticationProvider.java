package sg.ncl.service.authentication.validation;

import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;

import javax.inject.Inject;

/**
 * @author Te Ye
 */
public class JwtAuthenticationProvider {

    @Inject
    private JwtUtil jwtUtil;

}
