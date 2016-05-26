package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exceptions.NotFoundException;

/**
 * Created by Desmond
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No users found")
public class NoUsersFoundException extends NotFoundException {}