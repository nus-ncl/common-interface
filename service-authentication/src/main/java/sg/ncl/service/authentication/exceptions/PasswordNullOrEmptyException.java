package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class PasswordNullOrEmptyException extends BadRequestException {

    public PasswordNullOrEmptyException() {}

    public PasswordNullOrEmptyException(final String message) {
        super(message);
    }

    public PasswordNullOrEmptyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PasswordNullOrEmptyException(final Throwable cause) {
        super(cause);
    }

    protected PasswordNullOrEmptyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
