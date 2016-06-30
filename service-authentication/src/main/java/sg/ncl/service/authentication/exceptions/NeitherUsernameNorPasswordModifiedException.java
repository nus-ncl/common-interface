package sg.ncl.service.authentication.exceptions;

import sg.ncl.common.exception.NotModifiedException;

/**
 * @author Christopher Zhong
 */
public class NeitherUsernameNorPasswordModifiedException extends NotModifiedException {

    public NeitherUsernameNorPasswordModifiedException() {
        super("Username and/or password is unchanged");
    }

}
