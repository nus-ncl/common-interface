package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcsjnh on 3/28/2017.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email has not been verified")
public class EmailNotVerifiedException extends BadRequestException {
    public EmailNotVerifiedException(final String msg) {
        super(msg);
    }
}
