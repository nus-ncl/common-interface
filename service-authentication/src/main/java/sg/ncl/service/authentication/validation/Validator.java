package sg.ncl.service.authentication.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.exceptions.NeitherUsernameNorPasswordModifiedException;
import sg.ncl.service.authentication.exceptions.PasswordNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.UserIdNullOrEmptyException;
import sg.ncl.service.authentication.exceptions.UsernameNullOrEmptyException;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public static void validateForCreation(final Credentials credentials) {
        if (isIdNullOrEmpty(credentials)) {
            logger.warn("User Id is either null or empty");
            throw new UserIdNullOrEmptyException();
        }
        if (isUsernameNullOrEmpty(credentials)) {
            logger.warn("Username is either null or empty");
            throw new UsernameNullOrEmptyException();
        }
        if (isPasswordNullOrEmpty(credentials)) {
            logger.warn("Password is either null or empty");
            throw new PasswordNullOrEmptyException();
        }
    }

    public static void validateForUpdate(final Credentials credentials) {
        if (isUsernameNullOrEmpty(credentials) && isPasswordNullOrEmpty(credentials)) {
            logger.warn("Both username and password not modified");
            throw new NeitherUsernameNorPasswordModifiedException();
        }
    }

    private static boolean isIdNullOrEmpty(final Credentials credentials) {return credentials.getId() == null || credentials.getId().isEmpty();}

    private static boolean isUsernameNullOrEmpty(final Credentials credentials) {return credentials.getUsername() == null || credentials.getUsername().isEmpty();}

    private static boolean isPasswordNullOrEmpty(final Credentials credentials) {return credentials.getPassword() == null || credentials.getPassword().isEmpty();}

}
