package sg.ncl.service.analytics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no project details.")
public class ProjectDetailsNotFoundException extends NotFoundException {

    public ProjectDetailsNotFoundException(final String message) {
        super(message);
    }

}
