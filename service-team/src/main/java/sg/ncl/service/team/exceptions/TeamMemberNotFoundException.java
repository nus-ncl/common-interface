package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.NotFoundException;

/**
 * @author Te Ye
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Team member is not in team")
public class TeamMemberNotFoundException extends NotFoundException {
}
