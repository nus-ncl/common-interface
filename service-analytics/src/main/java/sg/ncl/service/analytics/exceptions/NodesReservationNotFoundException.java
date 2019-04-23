package sg.ncl.service.analytics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no node reservation details.")
public class NodesReservationNotFoundException extends ConflictException {

    public NodesReservationNotFoundException(final String message) {
        super(message);
    }
}
