package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author Te Ye
 */
public class JoinProjectException extends BadRequestException {

    public JoinProjectException() {
        super("Error in join team request");
    }

}
