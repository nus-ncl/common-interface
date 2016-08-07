package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class EmptyAuthorizationHeaderException extends BadRequestException {

    public EmptyAuthorizationHeaderException() {
        super("Authorization header cannot be empty");
    }

}
