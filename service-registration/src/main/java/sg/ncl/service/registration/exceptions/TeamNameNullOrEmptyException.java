package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Team name is empty")
public class TeamNameNullOrEmptyException extends NotFoundException {}