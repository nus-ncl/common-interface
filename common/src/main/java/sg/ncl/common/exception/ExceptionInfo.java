package sg.ncl.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A simplified representation of an {@link Exception} that to be returned through by the {@link GlobalExceptionHandler}.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@Getter
public final class ExceptionInfo implements Serializable {

    private final long timestamp = ZonedDateTime.now().toEpochSecond();
    private final String error;
    private final String message;
    private final int status;
    private final String path;

    public ExceptionInfo(final Exception exception, final HttpStatus status, final WebRequest request) {
        error = exception.getClass().getName();
        message = exception.getMessage();
        this.status = status.value();
        path = ((ServletWebRequest) request).getRequest().getRequestURI();
    }

}
