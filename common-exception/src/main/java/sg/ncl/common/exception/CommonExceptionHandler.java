package sg.ncl.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@ControllerAdvice
class CommonExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler({BadRequestException.class, ServletRequestBindingException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionInfo handleBadRequestExceptions(final BadRequestException exception) {
        logger.warn("{}: {}", exception.getClass().getName(), exception.getLocalizedMessage());
        return new ExceptionInfo(exception);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionInfo handleConflictExceptions(final ConflictException exception) {
        logger.warn("{}: {}", exception.getClass().getName(), exception.getLocalizedMessage());
        return new ExceptionInfo(exception);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionInfo handleNotFoundExceptions(final NotFoundException exception) {
        logger.warn("{}: {}", exception.getClass().getName(), exception.getLocalizedMessage());
        return new ExceptionInfo(exception);
    }

    @ExceptionHandler(NotModifiedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public ExceptionInfo handleNotModifiedExceptions(final NotModifiedException exception) {
        logger.warn("{}: {}", exception.getClass().getName(), exception.getLocalizedMessage());
        return new ExceptionInfo(exception);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionInfo handleUnauthorizedExceptions(final UnauthorizedException exception) {
        logger.warn("{}: {}", exception.getClass().getName(), exception.getLocalizedMessage());
        return new ExceptionInfo(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionInfo handleExceptions(final Exception exception) {
        logger.warn("{}: {}", exception.getClass().getName(), exception.getLocalizedMessage());
        return new ExceptionInfo(exception);
    }

}
