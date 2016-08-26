package sg.ncl.adapter.deterlab.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class ExpDeleteException extends BadRequestException {

    public ExpDeleteException() {
        super("Error in delete experiment");
    }

}
