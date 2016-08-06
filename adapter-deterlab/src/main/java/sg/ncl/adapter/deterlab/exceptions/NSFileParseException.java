package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.BadRequestException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error parsing ns file")
public class NSFileParseException extends BadRequestException {

    public NSFileParseException() {
        super("Error parsing ns file");
    }

}
