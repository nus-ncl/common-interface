package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class NullUsernameException extends BadRequestException {

    public NullUsernameException() {}

    public NullUsernameException(final String message) {
        super(message);
    }

    public NullUsernameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NullUsernameException(final Throwable cause) {
        super(cause);
    }

    protected NullUsernameException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
