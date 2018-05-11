package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Format error when adding public key")
public class InvalidPublicKeyFormatException extends BadRequestException {

    public InvalidPublicKeyFormatException(final String msg) {
        super(msg);
    }
}
