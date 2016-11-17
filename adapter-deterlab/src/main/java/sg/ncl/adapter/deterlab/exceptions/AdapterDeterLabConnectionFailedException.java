package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author Te Ye
 */
@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Connection refused to remote adapter deterlab")
public class AdapterDeterLabConnectionFailedException extends ResourceAccessException {

    public AdapterDeterLabConnectionFailedException() {
        super("Connection refused to remote adapter deterlab");
    }

    public AdapterDeterLabConnectionFailedException(final String message) {
        super(message);
    }
}
