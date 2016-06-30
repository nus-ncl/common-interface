package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.ConflictException;

/**
 * @author Christopher Zhong
 */
public class UserIdAlreadyExistsException extends ConflictException {

    public UserIdAlreadyExistsException(final String message) {
        super(message);
    }

}
