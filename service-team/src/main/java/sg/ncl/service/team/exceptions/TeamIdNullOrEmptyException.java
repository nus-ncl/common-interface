package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.NotFoundException;

/**
 * Created by Chunwang
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Team ID is null or empty")
public class TeamIdNullOrEmptyException extends NotFoundException {
}