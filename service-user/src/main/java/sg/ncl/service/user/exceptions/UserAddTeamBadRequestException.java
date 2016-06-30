package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.BadRequestException;

/**
 * Created by Desmond
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request when adding user to team")
public class UserAddTeamBadRequestException extends BadRequestException {
}
