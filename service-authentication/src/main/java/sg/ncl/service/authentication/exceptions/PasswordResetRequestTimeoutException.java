package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 11/4/2016.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Password reset key timeout")
public class PasswordResetRequestTimeoutException extends BadRequestException {
    public PasswordResetRequestTimeoutException(final String message) {
        super(message);
    }
}
