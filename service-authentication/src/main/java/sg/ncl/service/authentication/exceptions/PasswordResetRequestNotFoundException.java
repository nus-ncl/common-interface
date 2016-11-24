package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by dcszwang on 11/4/2016.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request not found")
public class PasswordResetRequestNotFoundException extends NotFoundException {
    public PasswordResetRequestNotFoundException(final String message) {
        super(message);
    }
}
