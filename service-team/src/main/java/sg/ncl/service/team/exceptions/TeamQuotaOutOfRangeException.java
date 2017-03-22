package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @Author Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team quota is negative or more than limit")
public class TeamQuotaOutOfRangeException extends BadRequestException {
    public TeamQuotaOutOfRangeException(final String message) {
        super(message);
    }
}
