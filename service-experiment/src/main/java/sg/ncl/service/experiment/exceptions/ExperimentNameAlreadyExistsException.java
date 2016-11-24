package sg.ncl.service.experiment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Experiment already exists")
public class ExperimentNameAlreadyExistsException extends ConflictException {
    public ExperimentNameAlreadyExistsException(final String message) {
        super(message);
    }
}
