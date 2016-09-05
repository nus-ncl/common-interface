package sg.ncl.service.authentication.validation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author Te Ye
 */
public class JwtUtil {

    private void parseToken(String token) {
        Claims claims = Jwts.parser()
                .parseClaimsJws(token)
                .getBody();
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Issued At: " + claims.getIssuedAt());
        System.out.println("Expiration: " + claims.getExpiration());
    }

}
