package sg.ncl.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Te Ye, Christopher Zhong
 */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        // extract the authorization header that is needed Jwt authentication
        if (authorization != null && authorization.startsWith(BEARER)) {
            final JwtToken jwtToken = new JwtToken(authorization.replaceAll(BEARER, ""));
            log.info("Using authorization = '{}', method = {}, uri = {}, token = {}", authorization, request.getMethod(), request.getRequestURI(), jwtToken);
            SecurityContextHolder.getContext().setAuthentication(jwtToken);
        }
        filterChain.doFilter(request, response);
    }

}
