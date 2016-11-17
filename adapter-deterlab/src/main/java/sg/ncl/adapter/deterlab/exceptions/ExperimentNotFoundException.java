package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by dcszwang on 11/17/2016.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Experiment not found")
public class ExperimentNotFoundException extends NotFoundException {
    public ExperimentNotFoundException(final String message) {
        super(message);
    }
}
