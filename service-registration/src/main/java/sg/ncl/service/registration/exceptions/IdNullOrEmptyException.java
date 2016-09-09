package sg.ncl.service.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;

/**
 * Created by dcszwang on 8/26/2016.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ID is null or empty")
public class IdNullOrEmptyException extends BadRequestException {
}

