package sg.ncl.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Te Ye, Christopher Zhong
 */
@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtParser jwtParser;

    JwtAuthenticationProvider(@NotNull final JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        log.info("Authenticating: {}", authentication);
        try {
            final String token = authentication.getCredentials().toString();
            final Claims claims = jwtParser.parseClaimsJws(token).getBody();
            checkClaims(claims);
            return new JwtToken(token, claims);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | PrematureJwtException e) {
            log.warn("{}", e);
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    private void checkClaims(Claims claims) {
        final Date now = Date.from(ZonedDateTime.now().toInstant());
        // check the issued-at date, a claims that is issued at a future date cannot be used
        // a null issued-at date is considered a date set in the future
        final Date issuedAt = claims.getIssuedAt();
        if (issuedAt == null || now.before(issuedAt)) {
            log.warn("Attempting to use a claims before it has been issued: {}", issuedAt);
            throw new BadCredentialsException("Claims cannot be used before " + issuedAt);
        }
        // check the expiration date, a claims that has expired cannot be used
        // a null date is considered a date set in the past (i.e., expired)
        final Date expiration = claims.getExpiration();
        if (expiration == null || now.after(expiration)) {
            log.warn("Attempting to use an expired claims: {}", expiration);
            throw new BadCredentialsException("Claims has expired: " + expiration);
        }
        // check the not-before date, a claims cannot be used until after the not-before date
        // a null not-before date is considered a date set in the future
        final Date notBefore = claims.getNotBefore();
        if (notBefore == null || now.before(notBefore)) {
            log.warn("Attempting to use a claims before usable date: {}", notBefore);
            throw new BadCredentialsException("Claims cannot be used before " + notBefore);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }

}
