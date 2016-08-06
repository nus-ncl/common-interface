package sg.ncl.common.exception;

import lombok.Getter;

import java.io.Serializable;

/**
 * A simplified representation of an {@link Exception} that to be returned through by the {@link GlobalExceptionHandler}.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
@Getter
public final class ExceptionInfo implements Serializable {

    private final String name;
    private final String message;
    private final String localizedMessage;

    ExceptionInfo(final Exception exception) {
        name = exception.getClass().getName();
        message = exception.getMessage();
        localizedMessage = exception.getLocalizedMessage();
    }

}
