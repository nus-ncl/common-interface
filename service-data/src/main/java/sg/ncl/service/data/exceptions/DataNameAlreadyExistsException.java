package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * Created by jng on 18/10/16.
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Data name already exists.")
public class DataNameAlreadyExistsException extends ConflictException {
    public DataNameAlreadyExistsException(final String message) {
        super(message);
    }
}
