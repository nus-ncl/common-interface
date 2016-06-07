package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.ConflictException;

/**
 * @author Christopher Zhong
 */
public class UserIdAlreadyExistsException extends ConflictException {

    public UserIdAlreadyExistsException() {}

    public UserIdAlreadyExistsException(final String message) {
        super(message);
    }

    public UserIdAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserIdAlreadyExistsException(final Throwable cause) {
        super(cause);
    }

    protected UserIdAlreadyExistsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
