package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.NotFoundException;

/**
 * @author Te Ye
 */
@ResponseStatus(value= HttpStatus.REQUEST_TIMEOUT, reason = "Connection refused to remote adapter deterlab")
public class AdapterDeterlabConnectException extends NotFoundException {

    public AdapterDeterlabConnectException() {
        super("Connection refused to remote adapter deterlab");
    }
}
