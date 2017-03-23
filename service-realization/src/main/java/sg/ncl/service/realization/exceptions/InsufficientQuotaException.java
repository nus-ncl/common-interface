package sg.ncl.service.realization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @Author Tran Ly Vu
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient quota to start experiment")
public class InsufficientQuotaException extends BadRequestException{
    public InsufficientQuotaException (final String message) {
        super(message);
    }
}
