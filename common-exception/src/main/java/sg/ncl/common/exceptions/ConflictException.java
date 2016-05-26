package sg.ncl.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Christopher Zhong
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {}
