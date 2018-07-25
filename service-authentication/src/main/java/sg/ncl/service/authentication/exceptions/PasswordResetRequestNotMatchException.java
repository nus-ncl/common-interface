package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 7/23/2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The password reset request and user do not match")
public class PasswordResetRequestNotMatchException extends BadRequestException{
    public PasswordResetRequestNotMatchException(final String message) {
        super(message);
    }
}
