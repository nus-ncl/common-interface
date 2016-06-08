package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class UnknownAuthorizationSchemeException extends BadRequestException {

    public UnknownAuthorizationSchemeException() {}

    public UnknownAuthorizationSchemeException(final String message) {
        super(message);
    }

    public UnknownAuthorizationSchemeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnknownAuthorizationSchemeException(final Throwable cause) {
        super(cause);
    }

    protected UnknownAuthorizationSchemeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
