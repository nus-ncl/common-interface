package sg.ncl.adapter.deterlab.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class ExperimentStartException extends BadRequestException {

    public ExperimentStartException() {
        super("Error in start experiment");
    }

}
