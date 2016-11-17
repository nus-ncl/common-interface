package sg.ncl.adapter.deterlab.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class ExperimentDeleteException extends BadRequestException {

    public ExperimentDeleteException() {
        super("Error in delete experiment");
    }

}
