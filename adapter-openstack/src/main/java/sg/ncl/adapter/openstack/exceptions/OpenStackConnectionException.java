package sg.ncl.adapter.openstack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Tran Ly Vu
 */
@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Connection refused to OpenStack controller")
public class OpenStackConnectionException extends RuntimeException{

    public OpenStackConnectionException() {
        super("Connection refused to OpenStack controller");
    }

    public OpenStackConnectionException(final String message) {
        super(message);
    }
}
