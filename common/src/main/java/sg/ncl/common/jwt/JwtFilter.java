package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.Key;

/**
 * @author Te Ye
 */
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private Key apiKey;
    private String get = "GET";
    private String post = "POST";

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

        if (!isWhitelistedUrl(req)) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header: {}", authHeader);
                throw new ServletException("Missing or invalid Authorization header.");
            }

            final String token = authHeader.substring(7); // The part after "Bearer "
            log.info("Send request with Authorization: {} to {}", authHeader, ((HttpServletRequest) req).getRequestURI());
            try {
                final Claims claims = Jwts.parser().setSigningKey(apiKey)
                        .parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
                log.info("Subject: {}", claims.getSubject());
                log.info("Issued At: {}", claims.getIssuedAt());
                log.info("Expiration: {}", claims.getExpiration());
                log.info("Roles: {}", claims.get("roles"));

                // check requester's roles
                // using this way instead of comparing against Role.USER/ADMIN to prevent coupling of "service-authentication" with "common"
                if (claims.get("roles") == null || (!claims.get("roles").toString().equals("[USER]") && !claims.get("roles").toString().equals("[ADMIN]"))) {
                    log.warn("Invalid roles: {}", claims.get("roles"));
                    throw new ServletException("Invalid roles.");
                }

            } catch (final SignatureException e) {
                log.warn("Invalid token: {}", e);
                throw new ServletException("Invalid token.");
            }
        }
        chain.doFilter(req, res);
    }

    /**
     * Checks if the request URI is whitelisted, whitelisted URI do not have to be check for their Authentication Header
     *
     * @param req the servlet request from web service
     * @return True if the request URI is whitelisted, returns False otherwise
     */
    private boolean isWhitelistedUrl(ServletRequest req) {
        String requestURI = ((HttpServletRequest) req).getRequestURI();
        String requestMethod = ((HttpServletRequest) req).getMethod();

        if (requestURI.startsWith("/authentication") ||
                (requestURI.startsWith("/users") && (requestMethod.equals(get))) ||
                (requestURI.startsWith("registrations") && (requestMethod.equals(post))) ||
                isTeamUrlWhitelist(req)
                ) {
            log.info("Whitelist: {} - {}", requestURI, requestMethod);
            return true;
        }

//        if (requestURI.startsWith("/teams/") && requestMethod.equals(get)) {
//            String[] param = req.getParameterValues("visibility");
//            String[] nameParam = req.getParameterValues("name");
//            if (param != null && (param.length != 0) && (param[0].equals("PUBLIC"))) {
//                log.info("Whitelist: {} - {}", requestURI, requestMethod);
//                return true;
//            }
//            if (nameParam != null && (nameParam.length != 0)) {
//                log.info("Whitelist: {} - {}", requestURI, requestMethod);
//                return true;
//            }
//        } else if (requestURI.startsWith("/users/") && requestMethod.equals(get)) {
//            log.info("Whitelist: {} - {}", requestURI, requestMethod);
//            return true;
//        } else if (requestURI.startsWith("/authentication")) {
//            log.info("Whitelist: {} - {}", requestURI, requestMethod);
//            return true;
//        } else if (requestURI.startsWith("/registrations") && requestMethod.equals(post)) {
//            log.info("Whitelist: {} - {}", requestURI, requestMethod);
//            return true;
//        }
        return false;
    }

    /**
     * Checks if the team URL requested is whitelisted, whitelisted URI do not have to be check for their Authentication Header
     * @param req the servlet request from web service
     * @return True if the request URI is whitelisted, returns False otherwise
     */
    private boolean isTeamUrlWhitelist(ServletRequest req) {
        String requestURI = ((HttpServletRequest) req).getRequestURI();
        if (requestURI.startsWith("/teams/")) {
            String[] param = req.getParameterValues("visibility");
            String[] nameParam = req.getParameterValues("name");

            if ((param != null && param.length > 0 && param[0].equals("PUBLIC")) ||
                    (nameParam != null && nameParam.length > 0)
                    ) {
                return true;
            }
        }
        return false;
    }

}
