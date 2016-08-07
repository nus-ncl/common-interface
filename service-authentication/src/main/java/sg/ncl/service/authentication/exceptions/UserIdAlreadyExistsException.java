package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.ConflictException;

/**
 * @author Christopher Zhong
 */
public class UserIdAlreadyExistsException extends ConflictException {

    public UserIdAlreadyExistsException(final String message) {
        super(message);
    }

}
