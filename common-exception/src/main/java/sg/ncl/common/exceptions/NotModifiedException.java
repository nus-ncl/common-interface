package sg.ncl.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Christopher Zhong
 */
@ResponseStatus(HttpStatus.NOT_MODIFIED)
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
