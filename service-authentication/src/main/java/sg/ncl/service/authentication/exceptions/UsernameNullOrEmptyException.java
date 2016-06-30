package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class UsernameNullOrEmptyException extends BadRequestException {

    public UsernameNullOrEmptyException() {
        super("Username cannot be null or empty");
    }

}
