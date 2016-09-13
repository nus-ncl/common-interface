package sg.ncl.service.authentication.validation;

import lombok.extern.slf4j.Slf4j;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.exceptions.NeitherUsernameNorPasswordModifiedException;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.RolesIsNullOrEmptyException;
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

    public static void check(final Credentials credentials) {
        checkId(credentials);
        checkUsername(credentials);
        checkPassword(credentials);
        checkStatus(credentials);
        checkRoles(credentials);
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
            throw new RolesIsNullOrEmptyException();
        }
    }

    public static void validateForCreation(final Credentials credentials) {
        if (isIdNullOrEmpty(credentials)) {
            log.warn("User Id is either null or empty");
            throw new UserIdNullOrEmptyException();
        }
        if (isUsernameNullOrEmpty(credentials)) {
            log.warn("Username is either null or empty");
            throw new UsernameNullOrEmptyException();
        }
        if (isPasswordNullOrEmpty(credentials)) {
            log.warn("Password is either null or empty");
            throw new PasswordNullOrEmptyException();
        }
    }

    public static void validateForUpdate(final Credentials credentials) {
        if (isUsernameNullOrEmpty(credentials) && isPasswordNullOrEmpty(credentials)) {
            log.warn("Both username and password not modified");
            throw new NeitherUsernameNorPasswordModifiedException();
        }
    }

    private static boolean isIdNullOrEmpty(final Credentials credentials) {return credentials.getId() == null || credentials.getId().isEmpty();}

    private static boolean isUsernameNullOrEmpty(final Credentials credentials) {return credentials.getUsername() == null || credentials.getUsername().isEmpty();}

    private static boolean isPasswordNullOrEmpty(final Credentials credentials) {return credentials.getPassword() == null || credentials.getPassword().isEmpty();}

}
