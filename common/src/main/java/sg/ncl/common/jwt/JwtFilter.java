package sg.ncl.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Te Ye
 */
@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.contains("Basic")) {
            // for login and whitelisted urls
            log.info("Don't require authorization header url: {} {}", request.getMethod(), request.getRequestURL());
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            log.info("Require authorization header url: {} {}", request.getMethod(), request.getRequestURL());
            authHeader.replaceAll("Bearer ", "");
            JwtToken token = new JwtToken(authHeader);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(req, res);
    }
}
