package sg.ncl.common.exception.base;

/**
 * @author Christopher Zhong
 */
public class NotModifiedException extends RuntimeException {

    public NotModifiedException() {}

    public NotModifiedException(final String message) {
        super(message);
    }

    public NotModifiedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotModifiedException(final Throwable cause) {
        super(cause);
    }

    protected NotModifiedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
