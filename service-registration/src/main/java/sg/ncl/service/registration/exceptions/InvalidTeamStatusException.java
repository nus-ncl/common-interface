package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by zhang on 8/25/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Team Status Invalid")
public class InvalidTeamStatusException extends BadRequestException{
}
