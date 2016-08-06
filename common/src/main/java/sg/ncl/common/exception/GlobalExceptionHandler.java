package sg.ncl.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ExceptionHttpStatusMap exceptionHttpStatusMap;

    @Inject
    GlobalExceptionHandler(@NotNull final ExceptionHttpStatusMap exceptionHttpStatusMap) {
        this.exceptionHttpStatusMap = exceptionHttpStatusMap;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleBadRequestExceptions(final Exception exception, final WebRequest request) {
        log.warn("{}: message = '{}': localizedMessage = '{}'", exception.getClass().getName(), exception.getLocalizedMessage());
        return handleExceptionInternal(exception, new ExceptionInfo(exception), new HttpHeaders(), exceptionHttpStatusMap.get(exception), request);
    }

}
