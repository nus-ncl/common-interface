package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author Christopher Zhong
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Credentials not found")
public class CredentialsNotFoundException extends NotFoundException {

    public CredentialsNotFoundException(final String message) {
        super(message);
    }

}
