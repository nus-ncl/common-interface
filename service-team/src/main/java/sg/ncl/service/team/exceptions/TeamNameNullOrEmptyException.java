package sg.ncl.service.team.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by Chunwang
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team name null or empty")
public class TeamNameNullOrEmptyException extends BadRequestException {
}

