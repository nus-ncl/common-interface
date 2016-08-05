package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.BadRequestException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Password cannot be updated")
public class CredentialsUpdateException extends BadRequestException {

    public CredentialsUpdateException() {
        super("Password cannot be updated");
    }

}
