package sg.ncl.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ExceptionHttpStatusMap exceptionHttpStatusMap;

    @Inject
    GlobalExceptionHandler(@NotNull final ExceptionHttpStatusMap exceptionHttpStatusMap) {
        this.exceptionHttpStatusMap = exceptionHttpStatusMap;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(final Exception exception, final WebRequest request) {
        return handleExceptionInternal(exception, new ExceptionInfo(exception), new HttpHeaders(), exceptionHttpStatusMap.get(exception), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception exception, final Object body, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final Object fBody = body == null ? new ExceptionInfo(exception) : body;
        log.warn("Caught exception = {}, body = {}, fBody = {}, headers = {}, status = {}, request = {}", exception, body, fBody, headers, status, request);
        return super.handleExceptionInternal(exception, fBody, headers, status, request);
    }
}
