package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 11/4/2016.
 */
public class PasswordResetRequestTimeoutException extends BadRequestException {
    public PasswordResetRequestTimeoutException(final String message) {
        super(message);
    }
}
