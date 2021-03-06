package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Team not found")
public class TeamNotFoundException extends NotFoundException {
    public TeamNotFoundException(final String message) {
        super(message);
    }
}
