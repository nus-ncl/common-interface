package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/29/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is not a team member")
public class UserIsNotTeamMemberException extends BadRequestException {
    public UserIsNotTeamMemberException(final String message) {
        super(message);
    }
}
