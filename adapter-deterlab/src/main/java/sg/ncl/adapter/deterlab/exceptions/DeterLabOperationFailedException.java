package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/29/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "DeterLab operation cannot be executed")
public class DeterLabOperationFailedException extends BadRequestException {
    public DeterLabOperationFailedException(final String message) {
        super(message);
    }
}
