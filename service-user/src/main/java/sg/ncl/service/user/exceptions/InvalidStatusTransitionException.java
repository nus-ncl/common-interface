package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/31/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid User Status Transition")
public class InvalidStatusTransitionException extends BadRequestException {
    public InvalidStatusTransitionException(final String message) {
        super(message);
    }
}
