package sg.ncl.adapter.deterlab.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class ExpStopException extends BadRequestException {

    public ExpStopException() {
        super("Error in stop experiment");
    }

}
