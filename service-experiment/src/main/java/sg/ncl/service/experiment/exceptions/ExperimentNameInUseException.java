package sg.ncl.service.experiment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @author Te Ye
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Experiment name already in use.")
public class ExperimentNameInUseException extends BadRequestException {}
