package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exceptions.ConflictException;

/**
 * @author Christopher Zhong
 */
public class UsernameAlreadyExistsException extends ConflictException {

    public UsernameAlreadyExistsException() {}

    public UsernameAlreadyExistsException(final String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExistsException(final Throwable cause) {
        super(cause);
    }

    protected UsernameAlreadyExistsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
