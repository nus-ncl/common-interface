package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User name already exists")
public class UserNameAlreadyExistsException extends ConflictException {

    public UserNameAlreadyExistsException(final String message) {
        super(message);
    }

}
