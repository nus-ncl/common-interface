package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exceptions.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class NullPasswordException extends BadRequestException {

    public NullPasswordException() {}

    public NullPasswordException(final String message) {
        super(message);
    }

    public NullPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NullPasswordException(final Throwable cause) {
        super(cause);
    }

    protected NullPasswordException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
