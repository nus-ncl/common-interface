package sg.ncl.service.experiment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by Desmond.
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User ID cannot be null or empty.")
public class UserIdNullOrEmptyException extends BadRequestException {
    public UserIdNullOrEmptyException() {
        super("User ID cannot be null or empty");
    }
}
