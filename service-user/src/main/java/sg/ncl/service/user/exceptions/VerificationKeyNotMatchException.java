package sg.ncl.service.user.exceptions;

import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/12/2016.
 */
public class VerificationKeyNotMatchException extends BadRequestException {

    public VerificationKeyNotMatchException(final String msg) {
        super(msg);
    }
}
