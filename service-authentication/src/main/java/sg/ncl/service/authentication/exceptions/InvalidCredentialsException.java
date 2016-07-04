package sg.ncl.service.authentication.exceptions;

/**
 * @author Christopher Zhong
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(final String message) {
        super(message);
    }

}
