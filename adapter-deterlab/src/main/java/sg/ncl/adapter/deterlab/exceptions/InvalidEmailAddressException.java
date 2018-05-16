package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author: Tran ly vu
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid email address")
public class InvalidEmailAddressException extends BadRequestException {
    public InvalidEmailAddressException() {
        super("Invalid email address");
    }
}
