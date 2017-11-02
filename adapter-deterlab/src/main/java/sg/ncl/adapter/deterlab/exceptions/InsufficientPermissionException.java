package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @Author: Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient permission")
public class InsufficientPermissionException extends BadRequestException{
    public InsufficientPermissionException(final String message) {
        super(message);
    }
}
