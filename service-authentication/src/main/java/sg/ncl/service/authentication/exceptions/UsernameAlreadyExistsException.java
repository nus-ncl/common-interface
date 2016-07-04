package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.ConflictException;

/**
 * @author Christopher Zhong
 */
public class UsernameAlreadyExistsException extends ConflictException {

    public UsernameAlreadyExistsException(final String message) {
        super(message);
    }

}
