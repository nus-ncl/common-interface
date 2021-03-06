package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email address already exists")
public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException(final String message) {
        super(message);
    }

}
