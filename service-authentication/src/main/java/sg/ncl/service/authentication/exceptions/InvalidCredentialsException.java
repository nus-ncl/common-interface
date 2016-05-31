package sg.ncl.service.authentication.exceptions;

/**
 * @author Christopher Zhong
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(final String message) {
        super(message);
    }

    public InvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(final Throwable cause) {
        super(cause);
    }

    protected InvalidCredentialsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
