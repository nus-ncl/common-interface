package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class NullUserIdException extends BadRequestException {

    public NullUserIdException() {}

    public NullUserIdException(final String message) {
        super(message);
    }

    public NullUserIdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NullUserIdException(final Throwable cause) {
        super(cause);
    }

    protected NullUserIdException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
