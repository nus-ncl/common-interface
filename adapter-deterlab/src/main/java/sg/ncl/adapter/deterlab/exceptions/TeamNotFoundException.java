package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author Te Ye
 */
@ResponseStatus(reason = "Team not found")
public class TeamNotFoundException extends NotFoundException {
    public TeamNotFoundException(final String message) {
        super(message);
    }
}
