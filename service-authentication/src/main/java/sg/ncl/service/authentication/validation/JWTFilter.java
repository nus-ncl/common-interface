package sg.ncl.service.authentication.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ParseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Te Ye
 */
@Slf4j
public class JWTFilter extends GenericFilterBean {

    private AuthenticationEntryPoint entryPoint;
    private AuthenticationManager authenticationManager;

    public JWTFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint entryPoint) {
        this.authenticationManager = authenticationManager;
        this.entryPoint = entryPoint;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            String stringToken = req.getHeader("Authorization");
            if (stringToken == null) {
                throw new InsufficientAuthenticationException("Authorization header not found");
            }

            // remove schema from token
            String authorizationSchema = "Bearer";
            if (stringToken.indexOf(authorizationSchema) == -1) {
                throw new InsufficientAuthenticationException("Authorization schema not found");
            }
            stringToken = stringToken.substring(authorizationSchema.length()).trim();

            try {
                System.out.println("String token: " + stringToken);
                chain.doFilter(request, response);
            } catch (ParseException e) {
                log.warn("Invalid token: {}", stringToken);
            }
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            if (entryPoint != null) {
                entryPoint.commence(req, res, e);
            }
        }
    }
}
