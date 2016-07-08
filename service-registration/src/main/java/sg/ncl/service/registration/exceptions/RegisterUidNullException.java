package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.NotFoundException;

/**
 * @author by Desmond / Te Ye
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Uid is null")
public class RegisterUidNullException extends NotFoundException {}
