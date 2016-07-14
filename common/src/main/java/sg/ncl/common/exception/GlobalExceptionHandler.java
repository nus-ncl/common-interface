package sg.ncl.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.inject.Inject;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ExceptionToHttpStatus mapping;

    @Inject
    protected GlobalExceptionHandler(final ExceptionToHttpStatus mapping) {
        this.mapping = mapping;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestExceptions(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, new ExceptionInfo(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictExceptions(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, new ExceptionInfo(ex), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundExceptions(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, new ExceptionInfo(ex), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NotModifiedException.class)
    public ResponseEntity<Object> handleNotModifiedExceptions(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, new ExceptionInfo(ex), new HttpHeaders(), HttpStatus.NOT_MODIFIED, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedExceptions(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, new ExceptionInfo(ex), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.warn("{}: {}", ex.getClass().getName(), ex.getLocalizedMessage());
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
