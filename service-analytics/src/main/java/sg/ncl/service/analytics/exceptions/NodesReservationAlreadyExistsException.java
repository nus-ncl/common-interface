package sg.ncl.service.analytics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Nodes reservation already exists.")
public class NodesReservationAlreadyExistsException extends ConflictException {

    public NodesReservationAlreadyExistsException(final String message) {
        super(message);
    }

}
