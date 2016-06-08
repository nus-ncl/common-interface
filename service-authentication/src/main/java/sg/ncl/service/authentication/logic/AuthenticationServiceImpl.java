package sg.ncl.service.authentication.logic;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.InvalidCredentialsException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.security.Key;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Christopher Zhong
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Key apiKey;
    private final Duration expiryDuration;

    @Inject
    protected AuthenticationServiceImpl(final CredentialsRepository credentialsRepository, final PasswordEncoder passwordEncoder, final SignatureAlgorithm signatureAlgorithm, final Key apiKey, final Duration expiryDuration) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.signatureAlgorithm = signatureAlgorithm;
        this.apiKey = apiKey;
        this.expiryDuration = expiryDuration;
    }

    @Transactional
    public String login(@NotNull final String username, @NotNull final String password) {
        // find the credentials first
        final CredentialsEntity credentials = credentialsRepository.findByUsername(username);
        if (credentials == null) {
            logger.warn("Credentials for '{}' not found", username);
            throw new CredentialsNotFoundException(username);
        }
        // compare the password
        if (passwordEncoder.matches(password, credentials.getPassword())) {
            // create and sign a JWT
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiry = now.plus(expiryDuration);
            final String jwt = Jwts.builder()
                    .setSubject(credentials.getId())
                    .setIssuer(AuthenticationService.class.getName())
                    .setIssuedAt(Date.from(now.toInstant()))
                    // expiry is set for 1 day from issue date
                    .setExpiration(Date.from(expiry.toInstant()))
                    // TODO custom claims such as permissions
//                    .claim()
                    // sign the JWT with the given algorithm and apiKey
                    .signWith(signatureAlgorithm, apiKey)
                    .compact();
            return jwt;
        }
        // TODO lockout behavior
        throw new InvalidCredentialsException(username);
    }

}
