package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class UserIdNullOrEmptyException extends BadRequestException {

    public UserIdNullOrEmptyException() {
        super("User ID cannot be null or empty");
    }

}
