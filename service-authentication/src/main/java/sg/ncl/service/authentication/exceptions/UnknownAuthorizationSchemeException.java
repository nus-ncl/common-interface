package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.BadRequestException;

/**
 * @author Christopher Zhong
 */
public class UnknownAuthorizationSchemeException extends BadRequestException {

    public UnknownAuthorizationSchemeException(final String message) {
        super(message);
    }

}
