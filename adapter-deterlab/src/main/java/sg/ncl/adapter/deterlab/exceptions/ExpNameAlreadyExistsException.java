package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Exp name already exists")
public class ExpNameAlreadyExistsException extends BadRequestException {

    public ExpNameAlreadyExistsException() {
        super("Exp name already exists");
    }

}
