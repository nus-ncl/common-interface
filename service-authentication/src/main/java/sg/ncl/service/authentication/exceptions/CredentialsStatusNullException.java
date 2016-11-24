package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by chris on 9/9/2016.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Credentials status cannot be null")
public class CredentialsStatusNullException extends BadRequestException {

    public CredentialsStatusNullException() {
        super("Credentials status cannot be null");
    }

}
