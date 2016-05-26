package sg.ncl.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Christopher Zhong
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    public ConflictException() {}

    public ConflictException(final String message) {
        super(message);
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConflictException(final Throwable cause) {
        super(cause);
    }

    protected ConflictException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
