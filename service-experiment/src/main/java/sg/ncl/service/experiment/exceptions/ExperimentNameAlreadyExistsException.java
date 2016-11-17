package sg.ncl.service.experiment.exceptions;

import sg.ncl.common.exception.base.ConflictException;

/**
 * @author Te Ye
 */

public class ExperimentNameAlreadyExistsException extends ConflictException {
    public ExperimentNameAlreadyExistsException(final String message) {
        super(message);
    }
}
