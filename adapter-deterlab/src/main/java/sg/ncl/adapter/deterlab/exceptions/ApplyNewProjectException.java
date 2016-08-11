package sg.ncl.adapter.deterlab.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class ApplyNewProjectException extends BadRequestException {

    public ApplyNewProjectException() {
        super("There is an error when applying new team");
    }

}
