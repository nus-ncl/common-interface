package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by jng on 18/10/16.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Data name already in use.")
public class DataNameInUseException extends BadRequestException {
}
