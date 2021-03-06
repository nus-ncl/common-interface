package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no data public user.")
public class DataPublicUserNotFoundException extends NotFoundException {

    public DataPublicUserNotFoundException(final String message) {
        super(message);
    }

}
