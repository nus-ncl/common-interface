package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by chris on 9/9/2016.
 */
public class RolesIsNullOrEmptyException extends BadRequestException {

    public RolesIsNullOrEmptyException() {
        super("Roles cannot be null or empty");
    }

}
