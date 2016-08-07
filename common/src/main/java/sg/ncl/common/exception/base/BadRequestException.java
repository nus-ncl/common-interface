package sg.ncl.common.exception.base;

/**
 * @author Christopher Zhong
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {}

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(final Throwable cause) {
        super(cause);
    }

    protected BadRequestException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
