package sg.ncl.service.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotModifiedException;

/**
 * Created by dcsjnh on 12/9/2016.
 */
@ResponseStatus(code = HttpStatus.NOT_MODIFIED, reason = "Error in deleting data resource.")
public class DataResourceDeleteException extends NotModifiedException {}
