package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Created by dcszwang on 11/15/2016.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Team member already exists")
public class TeamMemberAlreadyExistsException extends ConflictException {
    public TeamMemberAlreadyExistsException(final String message) {
        super(message);
    }
}
