package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team cannot have zero members")
public class NoMembersInTeamException extends BadRequestException {
    public NoMembersInTeamException() {
        super("Team cannot have zero members");
    }
}
