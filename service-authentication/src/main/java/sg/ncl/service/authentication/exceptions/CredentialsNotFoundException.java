package sg.ncl.service.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exceptions.NotFoundException;

/**
 * @author Christopher Zhong
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Credentials not found")
public class CredentialsNotFoundException extends NotFoundException {}
