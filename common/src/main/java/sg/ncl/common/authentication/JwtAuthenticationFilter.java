package sg.ncl.common.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Te Ye
 */
@Slf4j
public class JwtAuthenticationFilter {

//    public JwtAuthenticationFilter() {
//        super("/**");
//    }
//
//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        return true;
//    }
//
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        String header = request.getHeader("Authorization");
//
//        if (header != null && header.startsWith("Basic")) {
//            log.info("Do basic login without token: {}", header);
//            return null;
//        } else if (header == null || !header.startsWith("Bearer ")) {
//            log.warn("No JWT token found in request headers");
//        }
//
//        String authToken = header.substring(7);
//        Claims claims = Jwts.parser().setSigningKey("myKey").parseClaimsJws(authToken).getBody();
//        log.info("Header: {}", header);
//        log.info("Subject: {}", claims.getSubject());
//        log.info("Issuer: {}", claims.getIssuer());
//        log.info("Issued At: {}", claims.getIssuedAt());
//        log.info("Expiration: {}", claims.getExpiration());
//
//        JwtAuthenticationToken token = new JwtAuthenticationToken(authToken);
//        return getAuthenticationManager().authenticate(token);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
//            throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
//
//        // As this authentication is in HTTP header, after success we need to continue the request normally
//        // and return the response as if the resource was not secured at all
//        chain.doFilter(request, response);
//    }
}
