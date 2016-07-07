package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.NotFoundException;

/**
 * @author Christopher Zhong
 */
public class CredentialsNotFoundException extends NotFoundException {

    public CredentialsNotFoundException(final String message) {
        super(message);
    }

}
