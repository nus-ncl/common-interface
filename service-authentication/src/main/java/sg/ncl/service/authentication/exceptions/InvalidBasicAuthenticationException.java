package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class InvalidBasicAuthenticationException extends BadRequestException {

    public InvalidBasicAuthenticationException(final String message) {
        super(message);
    }

}
