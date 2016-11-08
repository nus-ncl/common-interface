package sg.ncl.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sg.ncl.common.authentication.AuthenticationProperties;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;

/**
 * @author Te Ye, Christopher Zhong
 */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    static final String BEARER = "Bearer ";

    private final AuthenticationProperties properties;

    @Inject
    JwtFilter(@NotNull AuthenticationProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if (!isWhitelisted(request)) {
            final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            // extract the authorization header that is needed Jwt authentication
            if (authorization != null && authorization.startsWith(BEARER)) {
                final JwtToken jwtToken = new JwtToken(authorization.replaceAll(BEARER, ""));
                log.info("Using authorization = '{}', method = {}, uri = {}, token = {}", authorization, request.getMethod(), request.getRequestURI(), jwtToken);
                SecurityContextHolder.getContext().setAuthentication(jwtToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isWhitelisted(final HttpServletRequest request) {
        String path = request.getServletPath();
        String reqMethod = request.getMethod();
        String queryString = request.getQueryString();
        String fullPath = constructFullPath(path, queryString);

        log.info("Filtering: {} - {}", fullPath, reqMethod);
        return properties.getUri().entrySet().stream().filter((entry -> fullPath.matches(entry.getKey()) && reqMethod.equals(entry.getValue()))).findFirst().isPresent();
    }

    /**
     * Construct the full request path based on the servlet path and query string
     * @param path e.g. /teams
     * @param queryString e.g. visibility=PUBLIC
     * @return the full path with the question mark added if query string exists, else the path is returned, i.e. /teams?visibility=PUBLIC
     */
    private String constructFullPath(String path, String queryString) {
        if (queryString != null && !queryString.isEmpty()) {
            return path + "?" + queryString;
        } else {
            return path;
        }
    }

}
