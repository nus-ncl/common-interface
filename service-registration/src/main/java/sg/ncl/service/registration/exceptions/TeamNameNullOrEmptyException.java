package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team name cannot be null or empty")
public class TeamNameNullOrEmptyException extends BadRequestException {
    public TeamNameNullOrEmptyException() {
        super("Team name cannot be null or empty");
    }
}
