package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @Author Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Only team owner can update team quota")
public class TeamOwnerException extends BadRequestException {
    public TeamOwnerException(final String message) {
        super(message);
    }
}
