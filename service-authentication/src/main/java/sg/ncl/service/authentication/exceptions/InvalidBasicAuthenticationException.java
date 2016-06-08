package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class InvalidBasicAuthenticationException extends BadRequestException {

    public InvalidBasicAuthenticationException() {}

    public InvalidBasicAuthenticationException(final String message) {
        super(message);
    }

    public InvalidBasicAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidBasicAuthenticationException(final Throwable cause) {
        super(cause);
    }

    protected InvalidBasicAuthenticationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
