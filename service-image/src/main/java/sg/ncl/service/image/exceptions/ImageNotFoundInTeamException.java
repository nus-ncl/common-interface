package sg.ncl.service.image.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @Author: Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Image is not found in the team")
public class ImageNotFoundInTeamException extends NotFoundException{
    public ImageNotFoundInTeamException(final String message) {
        super(message);
    }
}
