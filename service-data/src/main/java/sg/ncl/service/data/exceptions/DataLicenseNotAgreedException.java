package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Data license has not been agreed")
public class DataLicenseNotAgreedException extends BadRequestException {

    public DataLicenseNotAgreedException(final String message) {
        super(message);
    }

}
