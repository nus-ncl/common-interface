package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.NotModifiedException;

/**
 * @author Christopher Zhong
 */
public class NeitherUsernameNorPasswordModifiedException extends NotModifiedException {

    public NeitherUsernameNorPasswordModifiedException() {}

    public NeitherUsernameNorPasswordModifiedException(final String message) {
        super(message);
    }

    public NeitherUsernameNorPasswordModifiedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NeitherUsernameNorPasswordModifiedException(final Throwable cause) {
        super(cause);
    }

    protected NeitherUsernameNorPasswordModifiedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
