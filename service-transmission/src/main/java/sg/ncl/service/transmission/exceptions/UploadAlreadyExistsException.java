package sg.ncl.service.transmission.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Created by dcsjnh on 12/12/2016.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Upload already exists.")
public class UploadAlreadyExistsException extends ConflictException {
    public UploadAlreadyExistsException(final String message) {
        super(message);
    }
}
