package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team ID cannot be null or empty")
public class TeamIdNullOrEmptyException extends BadRequestException {
    public TeamIdNullOrEmptyException() {
        super("Team ID cannot be null or empty");
    }
}
