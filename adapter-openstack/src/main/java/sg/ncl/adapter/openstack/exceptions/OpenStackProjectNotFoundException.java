package sg.ncl.adapter.openstack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Author: Tran Ly Vu
 */

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "OpenStack project not found")
public class OpenStackProjectNotFoundException extends NotFoundException{
    public  OpenStackProjectNotFoundException(final String message) {
        super((message));
    }
}
