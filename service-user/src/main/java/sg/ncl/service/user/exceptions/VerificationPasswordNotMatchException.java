package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Verification password not match")
public class VerificationPasswordNotMatchException extends BadRequestException {

    public VerificationPasswordNotMatchException(final String msg) {
        super(msg);
    }
}
