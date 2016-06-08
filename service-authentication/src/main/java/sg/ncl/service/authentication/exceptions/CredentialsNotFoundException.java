package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.NotFoundException;

/**
 * @author Christopher Zhong
 */
public class CredentialsNotFoundException extends NotFoundException {

    public CredentialsNotFoundException() {}

    public CredentialsNotFoundException(final String message) {
        super(message);
    }

    public CredentialsNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CredentialsNotFoundException(final Throwable cause) {
        super(cause);
    }

    protected CredentialsNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
