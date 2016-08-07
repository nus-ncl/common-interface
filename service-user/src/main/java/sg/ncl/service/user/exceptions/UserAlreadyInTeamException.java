package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by chunwang
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already in team")
public class UserAlreadyInTeamException extends BadRequestException {}

