package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import sg.ncl.common.authentication.Role;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.Key;
import java.util.List;

/**
 * @author Te Ye
 */
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private Key apiKey;
    private String get = "GET";
    private String post = "POST";
    private String put = "PUT";

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
                checkRoles(request, token);
            } catch (final SignatureException | MalformedJwtException e) {
                log.warn("Invalid token: {}", e);
                throw new ServletException("Invalid token.");
            }
        }
        chain.doFilter(req, res);
    }

    private void checkRoles(HttpServletRequest request, String token) throws ServletException {
        final Claims claims = Jwts.parser().setSigningKey(apiKey)
                .parseClaimsJws(token).getBody();
        request.setAttribute("claims", claims);
        Object claimRole = claims.get("roles");
        log.info("Subject: {}", claims.getSubject());
        log.info("Issued At: {}", claims.getIssuedAt());
        log.info("Expiration: {}", claims.getExpiration());
        log.info("Roles: {}", claimRole);

        // check requester's roles
        // using this way instead of comparing against Role.USER/ADMIN to prevent coupling of "service-authentication" with "common"
        if (claimRole instanceof List) {
            List roleList = (List) claimRole;
            if (roleList == null || (!roleList.contains(Role.USER.toString()) && !roleList.contains(Role.ADMIN.toString()))) {
                log.warn("Invalid roles: {}", claims.get("roles"));
                throw new ServletException("Invalid roles.");
            }
        } else {
            throw new ServletException("Bad object.");
        }
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
                isUsersWhitelist(requestURI, requestMethod) ||
                (isRegWhitelist(requestURI, requestMethod)) ||
                isTeamUrlWhitelist(req)
                ) {
            log.info("Whitelist: {} - {}", requestURI, requestMethod);
            return true;
        }
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

            if (isParamPublic(param) ||
                    (nameParam != null && nameParam.length > 0)
                    ) {
                return true;
            }
        }
        return false;
    }

    private boolean isParamPublic(String[] param) {
        return param != null && param.length > 0 && param[0].equals("PUBLIC");
    }

    private boolean isUsersWhitelist(String requestURI, String requestMethod) {
        if (requestURI.startsWith("/users")) {
            if (requestMethod.equals(get)) {
                return true;
            } else if (requestURI.matches("\\/users\\/(.+)\\/emails\\/(.+)") && requestMethod.equals(put)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRegWhitelist(String requestURI, String requestMethod) {
        return (requestURI.equals("/registrations") || requestURI.equals("/registrations/"))  && (requestMethod.equals(post));
    }
}
