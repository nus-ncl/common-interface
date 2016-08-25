package sg.ncl.adapter.deterlab.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class ExpStartException extends BadRequestException {

    public ExpStartException() {
        super("Error in start experiment");
    }

}
