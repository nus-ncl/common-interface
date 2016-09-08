package sg.ncl.common.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.Key;

/**
 * @author Te Ye
 */
@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private Key apiKey;
    private String get = "GET";

    @Inject
    JwtFilter(@NotNull Key apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        // perform some whitelist
        log.info(((HttpServletRequest) req).getRequestURI() + " : " + ((HttpServletRequest) req).getMethod());

//        String reqURI = ((HttpServletRequest) req).getRequestURI();
//        String method = ((HttpServletRequest) req).getMethod();

//        if (reqURI.startsWith("/teams/") && method.equals(get)) {
//            String[] param = req.getParameterValues("visibility");
//            if ( (param.length != 0) && (param[0].equals("PUBLIC"))) {
//                log.info("Teams visibility here");
//                chain.doFilter(req, res);
//            }
//        }
//
//        if (reqURI.startsWith("/users/") && method.equals(get)) {
//            log.info("Users get here");
//            chain.doFilter(req, res);
//        }

//        if (reqURI.startsWith("/authentication")) {
//            log.info("Authentication here");
//            chain.doFilter(req, res);
//        }

//        if (reqURI.startsWith("/registrations")) {
//            log.info("Registrations here");
//            chain.doFilter(req, res);
//        }

        if (!isWhitelistedUrl(req)) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header: {}", authHeader);
                throw new ServletException("Missing or invalid Authorization header.");
            }

            final String token = authHeader.substring(7); // The part after "Bearer "
            log.info("Login with Authorization: {}", authHeader);
            try {
                final Claims claims = Jwts.parser().setSigningKey(apiKey)
                        .parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
            }
            catch (final SignatureException e) {
                log.warn("Invalid token: {}", e);
                throw new ServletException("Invalid token.");
            }
        }
        chain.doFilter(req, res);
    }

    private boolean isWhitelistedUrl(ServletRequest req) {
        String requestURI = ((HttpServletRequest) req).getRequestURI();
        String requestMethod = ((HttpServletRequest) req).getMethod();

        if (requestURI.startsWith("/teams/") && requestMethod.equals(get)) {
            String[] param = req.getParameterValues("visibility");
            if ( (param.length != 0) && (param[0].equals("PUBLIC"))) {
                log.info("Teams visibility here");
                return true;
            }
        } else if (requestURI.startsWith("/users/") && requestMethod.equals(get)) {
            log.info("Users get here");
            return true;
        } else if (requestURI.startsWith("/authentication")) {
            log.info("Authentication here");
            return true;
        } else if (requestURI.startsWith("/registrations")) {
            log.info("Registrations here");
            return true;
        }
        return false;
    }

}
