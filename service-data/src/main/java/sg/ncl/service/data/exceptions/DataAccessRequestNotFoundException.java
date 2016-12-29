package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by dcsjnh on 12/23/2016.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no data access request.")
public class DataAccessRequestNotFoundException extends NotFoundException {

    public DataAccessRequestNotFoundException(final String message) {
        super(message);
    }

}
