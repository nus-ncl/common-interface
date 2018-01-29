package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Author: Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "OpenStack project name already exists")
public class OpenStackProjectNameAlreadyExistsException extends ConflictException {
    public OpenStackProjectNameAlreadyExistsException(final String message) {
        super(message);
    }

}
