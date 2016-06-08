package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class UsernameNullOrEmptyException extends BadRequestException {

    public UsernameNullOrEmptyException() {}

    public UsernameNullOrEmptyException(final String message) {
        super(message);
    }

    public UsernameNullOrEmptyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UsernameNullOrEmptyException(final Throwable cause) {
        super(cause);
    }

    protected UsernameNullOrEmptyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
