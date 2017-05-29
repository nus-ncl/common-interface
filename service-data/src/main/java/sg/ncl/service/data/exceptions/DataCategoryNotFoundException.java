package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by dcsjnh on 12/5/2017.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no data category.")
public class DataCategoryNotFoundException extends NotFoundException {

    public DataCategoryNotFoundException(final String message) {
        super(message);
    }

}
