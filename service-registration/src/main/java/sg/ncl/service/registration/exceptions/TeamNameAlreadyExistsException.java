package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Team name already exists")
public class TeamNameAlreadyExistsException extends ConflictException {
    public TeamNameAlreadyExistsException(final String message) {
        super(message);
    }
}
