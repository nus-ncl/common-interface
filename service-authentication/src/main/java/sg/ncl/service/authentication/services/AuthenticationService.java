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
import java.security.Key;
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

    @Inject
    protected AuthenticationService(final CredentialsRepository credentialsRepository, final PasswordEncoder passwordEncoder, final SignatureAlgorithm signatureAlgorithm, final Key key) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.signatureAlgorithm = signatureAlgorithm;
        this.key = key;
    }

    @Transactional
    public String login(final String username, String password) {
        // FIXME not suppose to do this, but for dev purposes to bypass the credentials not found exception
        if (username.equals("johndoe@nus.edu.sg") && password.equals("password")) {
            return username;
        }
        // find the credentials first
        final CredentialsEntity credentials = credentialsRepository.findByUsername(username);
        if (credentials == null) {
            logger.warn("Credentials not found %1", username);
            throw new CredentialsNotFoundException(username);
        }
        // compare the password
        if (passwordEncoder.matches(password, credentials.getPassword())) {
            // create and sign a JWT
            final ZonedDateTime now = ZonedDateTime.now();
            final String jwt = Jwts.builder()
                    .setSubject(credentials.getUserId())
                    .setIssuer(AuthenticationService.class.getName())
                    .setIssuedAt(Date.from(now.toInstant()))
                    // expiry is set for 1 day from issue date
                    .setExpiration(Date.from(now.plusDays(1L).toInstant()))
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
