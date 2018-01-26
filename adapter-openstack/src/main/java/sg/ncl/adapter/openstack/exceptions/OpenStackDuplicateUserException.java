package sg.ncl.adapter.openstack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Author: Tran Ly Vu
 */

@ResponseStatus(value = HttpStatus.CONFLICT)

public class OpenStackDuplicateUserException extends ConflictException {
    public OpenStackDuplicateUserException(final String message) {
        super(message);
    }

}
