package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Christopher Zhong
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Authorization header cannot be empty")
public class EmptyAuthorizationHeaderException extends BadRequestException {

    public EmptyAuthorizationHeaderException() {
        super("Authorization header cannot be empty");
    }

}
