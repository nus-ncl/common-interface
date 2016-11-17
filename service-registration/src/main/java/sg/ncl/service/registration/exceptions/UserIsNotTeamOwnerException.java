package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ForbiddenException;

/**
 * @author Te Ye
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User is not a team owner")
public class UserIsNotTeamOwnerException extends ForbiddenException {
}
