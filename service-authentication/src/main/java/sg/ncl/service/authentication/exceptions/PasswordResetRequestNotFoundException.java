package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by dcszwang on 11/4/2016.
 */
public class PasswordResetRequestNotFoundException extends NotFoundException {
    public PasswordResetRequestNotFoundException(final String message) {
        super(message);
    }
}
