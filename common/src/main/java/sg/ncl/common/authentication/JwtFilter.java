package sg.ncl.common.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import sg.ncl.common.jwt.JwtProperties;

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
@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private Key apiKey;

    @Inject
    JwtFilter(@NotNull Key apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
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

        chain.doFilter(req, res);
    }

}
