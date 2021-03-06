package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class PasswordNullOrEmptyException extends BadRequestException {

    public PasswordNullOrEmptyException() {
        super("Password cannot be null or empty");
    }

}
