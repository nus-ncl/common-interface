package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by jng on 25/10/16.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no data resource.")
public class DataResourceNotFoundException extends NotFoundException {
}
