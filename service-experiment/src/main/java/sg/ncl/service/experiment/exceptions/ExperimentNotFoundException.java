package sg.ncl.service.experiment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by Desmond.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There are no experiments.")
public class ExperimentNotFoundException extends NotFoundException {}
