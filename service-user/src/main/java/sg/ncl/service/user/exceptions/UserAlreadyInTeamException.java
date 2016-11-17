package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Created by chunwang
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already in team")
public class UserAlreadyInTeamException extends ConflictException {
    public UserAlreadyInTeamException(final String message) {
        super(message);
    }
}

