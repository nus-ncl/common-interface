package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by Desmond
 */
@ResponseStatus(reason = "User not found")
public class UserNotFoundException extends NotFoundException {
}
