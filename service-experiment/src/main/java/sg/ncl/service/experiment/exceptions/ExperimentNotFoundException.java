package sg.ncl.service.experiment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by Desmond.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Experiment not found")
public class ExperimentNotFoundException extends NotFoundException {
    public ExperimentNotFoundException(final String message) {
        super(message);
    }
}
