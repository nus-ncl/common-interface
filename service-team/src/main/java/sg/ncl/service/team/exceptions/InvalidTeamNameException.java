package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 11/16/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team name invalid format")
public class InvalidTeamNameException extends BadRequestException {
    public InvalidTeamNameException(final String message) {
        super(message);
    }
}
