package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email address already exists")
public class EmailAlreadyExistsException extends BadRequestException {

    public EmailAlreadyExistsException() {
        super("Email address already exists");
    }

}
