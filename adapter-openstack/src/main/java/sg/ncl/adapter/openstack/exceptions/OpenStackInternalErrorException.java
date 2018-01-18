package sg.ncl.adapter.openstack.exceptions;

/**
 * Author: Tran Ly Vu
 */
public class OpenStackInternalErrorException extends RuntimeException{
    public OpenStackInternalErrorException() {
        super("OpenStack internal server error");
    }
}
