package sg.ncl.service.authentication.logic;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.exceptions.UserNotFoundException;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.authentication.data.jpa.CredentialsRepository;
import sg.ncl.service.authentication.domain.AuthenticationService;
import sg.ncl.service.authentication.domain.Authorization;
import sg.ncl.service.authentication.exceptions.CredentialsNotFoundException;
import sg.ncl.service.authentication.exceptions.EmailNotVerifiedException;
import sg.ncl.service.authentication.exceptions.InvalidCredentialsException;
import sg.ncl.service.authentication.exceptions.UserNotApprovedException;
import sg.ncl.service.authentication.web.AuthorizationInfo;
import sg.ncl.service.user.domain.User;
import sg.ncl.service.user.domain.UserService;
import sg.ncl.service.user.domain.UserStatus;

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
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Key apiKey;
    private final Duration expiryDuration;
    private final UserService userService;

    @Inject
    AuthenticationServiceImpl(
            @NotNull final CredentialsRepository credentialsRepository,
            @NotNull final PasswordEncoder passwordEncoder,
            @NotNull final SignatureAlgorithm signatureAlgorithm,
            @NotNull final Key apiKey,
            @NotNull final Duration expiryDuration,
            @NotNull final UserService userService
    ) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.signatureAlgorithm = signatureAlgorithm;
        this.apiKey = apiKey;
        this.expiryDuration = expiryDuration;
        this.userService = userService;
    }

    @Transactional
    public Authorization login(@NotNull final String username, @NotNull final String password) {
        // find the credentials first
        final CredentialsEntity credentials = credentialsRepository.findByUsername(username);
        if (credentials == null) {
            log.warn("Credentials for '{}' not found", username);
            throw new CredentialsNotFoundException(username);
        }
        // next, check the user status
        final User user = userService.getUser(credentials.getId());
        if(user == null) {
            log.warn("User {} not found", credentials.getId());
            throw new UserNotFoundException();
        }
        if(!user.isEmailVerified()){
            log.warn("Email has not been verified for {}", username);
            throw new EmailNotVerifiedException(username);
        }
        if(!user.getStatus().equals(UserStatus.APPROVED)) {
            log.warn("User {} has not been approved", username);
            throw new UserNotApprovedException(username);
        }
        // compare the password
        if (passwordEncoder.matches(password, credentials.getPassword())) {
            // create and sign a JWT
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiry = now.plus(expiryDuration);
            final JwtBuilder builder = Jwts.builder();
            final String jwt = Jwts.builder()
                    .setSubject(credentials.getId())
                    .setIssuer(AuthenticationService.class.getName())
                    .setIssuedAt(Date.from(now.toInstant()))
                    // the expiry should be short; can be set through properties
                    .setExpiration(Date.from(expiry.toInstant()))
                    // TODO custom claims such as permissions
//                    .claim()
                    // sign the JWT with the given algorithm and apiKey
                    .signWith(signatureAlgorithm, apiKey)
                    .compact();
            return new AuthorizationInfo(credentials.getId(), jwt);
        }
        // TODO lockout behavior
        throw new InvalidCredentialsException(username);
    }

}
