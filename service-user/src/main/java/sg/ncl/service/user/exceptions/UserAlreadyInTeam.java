package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exceptions.BadRequestException;

/**
 * Created by Desmond/Te Ye
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already in team")
public class UserAlreadyInTeam extends BadRequestException {}
