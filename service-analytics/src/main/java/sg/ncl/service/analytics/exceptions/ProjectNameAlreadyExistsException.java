package sg.ncl.service.analytics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Project name already exists.")
public class ProjectNameAlreadyExistsException extends ConflictException {

    public ProjectNameAlreadyExistsException(final String message) {
        super(message);
    }

}
