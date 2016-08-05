package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.NotFoundException;

/**
 * Created by Desmond
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User ID is empty")
public class UserIdNullException extends NotFoundException {}
