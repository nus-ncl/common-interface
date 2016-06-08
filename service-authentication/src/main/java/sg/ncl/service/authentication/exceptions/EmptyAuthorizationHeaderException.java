package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class EmptyAuthorizationHeaderException extends BadRequestException {

    public EmptyAuthorizationHeaderException() {}

    public EmptyAuthorizationHeaderException(final String message) {
        super(message);
    }

    public EmptyAuthorizationHeaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmptyAuthorizationHeaderException(final Throwable cause) {
        super(cause);
    }

    protected EmptyAuthorizationHeaderException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
