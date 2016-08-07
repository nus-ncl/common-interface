package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No owner in team")
public class NoOwnerInTeamException extends NotFoundException {}
