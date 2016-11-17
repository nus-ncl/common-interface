package sg.ncl.service.experiment.exceptions;

import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by Desmond.
 */
public class ExperimentNotFoundException extends NotFoundException {
    public ExperimentNotFoundException(final String message) {
        super(message);
    }
}
