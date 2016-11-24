package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/12/2016.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Verification email not match")
public class VerificationEmailNotMatchException extends BadRequestException {

    public VerificationEmailNotMatchException(final String msg) {
        super(msg);
    }
}
