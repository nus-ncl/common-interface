package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Created by dcsjnh on 12/12/2016.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Data resource already exists.")
public class DataResourceAlreadyExistsException extends ConflictException {
    public DataResourceAlreadyExistsException(final String message) {
        super(message);
    }
}
