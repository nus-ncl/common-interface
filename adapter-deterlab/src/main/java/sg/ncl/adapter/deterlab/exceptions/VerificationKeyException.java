package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by Tran Ly Vu on 11/21/2016.
 */

@ResponseStatus(reason = "Verification key error")
public class VerificationKeyException extends BadRequestException {
    public VerificationKeyException () { super("Verification key error");}

}
