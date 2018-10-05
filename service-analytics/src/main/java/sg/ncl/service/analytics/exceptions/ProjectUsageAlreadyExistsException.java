package sg.ncl.service.analytics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Project usage already exists.")
public class ProjectUsageAlreadyExistsException extends ConflictException {

    public ProjectUsageAlreadyExistsException(final String message) {
        super(message);
    }

}
