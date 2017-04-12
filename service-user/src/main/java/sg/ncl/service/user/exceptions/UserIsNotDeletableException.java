package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcsjnh on 4/4/2017.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User Is Not Allowed To Be Deleted")
public class UserIsNotDeletableException extends BadRequestException {
    public UserIsNotDeletableException(final String message) {
        super(message);
    }
}
