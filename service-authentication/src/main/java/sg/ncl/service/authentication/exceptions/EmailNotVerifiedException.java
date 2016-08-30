package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/30/2016.
 */
public class EmailNotVerifiedException extends BadRequestException {

    public EmailNotVerifiedException(final String message) {
        super(message);
    }
}
