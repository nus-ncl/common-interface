package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Connection refused to adapter deterlab")
public class AdapterDeterLabConnectionFailedException extends RuntimeException {

    public AdapterDeterLabConnectionFailedException() {
        super("Connection refused to adapter deterlab");
    }

    public AdapterDeterLabConnectionFailedException(final String message) {
        super(message);
    }
}
