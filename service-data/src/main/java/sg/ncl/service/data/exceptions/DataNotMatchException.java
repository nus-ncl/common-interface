package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcsjnh on 12/27/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Data IDs do not matched")
public class DataNotMatchException extends BadRequestException {

    public DataNotMatchException(final String message) {
        super(message);
    }

}
