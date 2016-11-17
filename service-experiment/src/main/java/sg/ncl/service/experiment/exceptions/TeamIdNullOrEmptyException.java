package sg.ncl.service.experiment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by Desmond.
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team ID cannot be null or empty.")
public class TeamIdNullOrEmptyException extends BadRequestException {
    public TeamIdNullOrEmptyException() {
        super("Team ID cannot be null or empty");
    }
}
