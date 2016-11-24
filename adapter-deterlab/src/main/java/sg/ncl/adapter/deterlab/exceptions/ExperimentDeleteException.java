package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error in delete experiment")
public class ExperimentDeleteException extends BadRequestException {

    public ExperimentDeleteException() {
        super("Error in delete experiment");
    }

}
