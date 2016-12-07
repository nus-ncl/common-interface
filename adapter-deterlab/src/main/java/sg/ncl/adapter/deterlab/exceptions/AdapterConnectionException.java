package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Connection refused to adapter deterlab")
public class AdapterConnectionException extends RuntimeException {

    public AdapterConnectionException() {
        super("Connection refused to adapter deterlab");
    }

    public AdapterConnectionException(final String message) {
        super(message);
    }
}
