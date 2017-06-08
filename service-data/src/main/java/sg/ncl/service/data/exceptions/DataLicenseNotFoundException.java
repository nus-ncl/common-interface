package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by dcsjnh on 30/5/2017.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no data license.")
public class DataLicenseNotFoundException extends NotFoundException {

    public DataLicenseNotFoundException(final String message) {
        super(message);
    }

}
