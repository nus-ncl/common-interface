package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Author: Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "OpenStack user name already exists")
public class OpenStackUserNameAlreadyExistsException extends ConflictException{
    public OpenStackUserNameAlreadyExistsException(final String message) {
        super(message);
    }
}
