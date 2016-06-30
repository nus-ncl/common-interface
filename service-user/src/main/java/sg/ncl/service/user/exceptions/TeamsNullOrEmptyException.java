package sg.ncl.service.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.BadRequestException;

/**
 * Created by Desmond
 */
public class TeamsNullOrEmptyException extends BadRequestException {
}
