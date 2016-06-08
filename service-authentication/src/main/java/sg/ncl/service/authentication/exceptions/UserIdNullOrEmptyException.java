package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class UserIdNullOrEmptyException extends BadRequestException {

    public UserIdNullOrEmptyException() {}

    public UserIdNullOrEmptyException(final String message) {
        super(message);
    }

    public UserIdNullOrEmptyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserIdNullOrEmptyException(final Throwable cause) {
        super(cause);
    }

    protected UserIdNullOrEmptyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
