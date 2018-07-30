package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author: Tran ly Vu
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Either first or last name is invalid")
public class InvalidUsernameException extends BadRequestException {
    public InvalidUsernameException(final String message) {
        super(message);
    }
}

