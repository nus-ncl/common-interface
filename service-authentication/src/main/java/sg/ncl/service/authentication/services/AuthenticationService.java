package sg.ncl.service.authentication.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.ncl.service.authentication.data.jpa.entities.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.repositories.CredentialsRepository;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.InvalidCredentialsException;

import javax.inject.Inject;
import javax.inject.Named;
import java.security.Key;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Christopher Zhong
 */
@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Key key;
    private final Duration duration;

    @Inject
    protected AuthenticationService(final CredentialsRepository credentialsRepository, final PasswordEncoder passwordEncoder, final SignatureAlgorithm signatureAlgorithm, final Key key, @Named("ncl.jwt.expiry.duration") final Duration duration) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.signatureAlgorithm = signatureAlgorithm;
        this.key = key;
        this.duration = duration;
    }

    @Transactional
    public String login(final String username, final String password) {
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
            final ZonedDateTime expiry = now.plus(duration);
            final String jwt = Jwts.builder()
                    .setSubject(credentials.getId())
                    .setIssuer(AuthenticationService.class.getName())
                    .setIssuedAt(Date.from(now.toInstant()))
                    // expiry is set for 1 day from issue date
                    .setExpiration(Date.from(expiry.toInstant()))
                    // TODO custom claims such as permissions
//                    .claim()
                    // sign the JWT with the given algorithm and key
                    .signWith(signatureAlgorithm, key)
                    .compact();
            return jwt;
        }
        // TODO lockout behavior
        throw new InvalidCredentialsException(username);
    }

    @Transactional(readOnly = true)
    public List<? extends Credentials> getAll() {
        return credentialsRepository.findAll();
    }

}
