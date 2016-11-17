package sg.ncl.service.authentication.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import sg.ncl.common.authentication.Role;
import sg.ncl.common.exception.base.ForbiddenException;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.NeitherUsernameNorPasswordModifiedException;
import sg.ncl.service.authentication.exceptions.RolesNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.StatusNullException;
import sg.ncl.service.authentication.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.UsernameNullOrEmptyException;

import java.util.Set;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Slf4j
public class Validator {

    public static void addCheck(final Credentials credentials) {
        checkId(credentials);
        checkUsername(credentials);
        checkPassword(credentials);
    }

    public static void updateCheck(final String id, final Credentials credentials, final Claims claims) {
        if (isUsernameNullOrEmpty(credentials) && isPasswordNullOrEmpty(credentials)) {
            log.warn("Both username and password not modified");
            throw new NeitherUsernameNorPasswordModifiedException();
        }
        // check said user id is identical
        if (claims.getSubject() == null || claims.getSubject().isEmpty() || !(id.equals(claims.getSubject()))) {
            log.warn("A user (id='{}') attempted to update the credentials of another user (id='{}')", claims.getSubject(), id);
            throw new ForbiddenException();
        }
    }

    public static void checkClaimsType(final Object claims) {
        if ( !(claims instanceof Claims) ) {
            // throw forbidden
            log.warn("Invalid authentication principal: {}", claims);
            throw new ForbiddenException();
        }
    }

    public static void checkId(final Credentials credentials) {
        final String id = credentials.getId();
        if (id == null || id.isEmpty()) {
            log.warn("ID is either null or empty: {}", id);
            throw new UserIdNullOrEmptyException();
        }
    }

    public static void checkUsername(final Credentials credentials) {
        final String username = credentials.getUsername();
        if (username == null || username.isEmpty()) {
            log.warn("Username is either null or empty: {}", username);
            throw new UsernameNullOrEmptyException();
        }
    }

    public static void checkPassword(final Credentials credentials) {
        final String password = credentials.getPassword();
        if (password == null || password.isEmpty()) {
            log.warn("Password is either null or empty: {}", password);
            throw new PasswordNullOrEmptyException();
        }
    }

    public static void checkStatus(final Credentials credentials) {
        final CredentialsStatus status = credentials.getStatus();
        if (status == null) {
            log.warn("Status is null: {}", status);
            throw new StatusNullException();
        }
    }

    public static void checkRoles(final Credentials credentials) {
        final Set<Role> roles = credentials.getRoles();
        if (roles == null || roles.isEmpty()) {
            log.warn("Roles is null or empty: {}", roles);
            throw new RolesNullOrEmptyException();
        }
    }

    private static boolean isUsernameNullOrEmpty(final Credentials credentials) {return credentials.getUsername() == null || credentials.getUsername().isEmpty();}

    private static boolean isPasswordNullOrEmpty(final Credentials credentials) {return credentials.getPassword() == null || credentials.getPassword().isEmpty();}

}
