package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by Te Ye
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team member privilege is invalid")
public class InvalidTeamMemberPrivilegeException extends BadRequestException {
    public InvalidTeamMemberPrivilegeException() {
        super("Team member privilege is invalid");
    }
}
