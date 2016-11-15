package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author Te Ye
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User is not a member of the team")
public class TeamMemberNotFoundException extends NotFoundException {
    public TeamMemberNotFoundException(final String message) {
        super(message);
    }
}
