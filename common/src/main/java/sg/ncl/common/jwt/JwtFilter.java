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
        log.info("{} {}", request.getMethod(), request.getRequestURL());

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            authHeader.replaceAll("Bearer ", "");
            JwtToken token = new JwtToken(authHeader);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(req, res);
    }
}
