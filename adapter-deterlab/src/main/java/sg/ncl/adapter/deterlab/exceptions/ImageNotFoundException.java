package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.NotFoundException;

/**
 * @Author: Tran Ly Vu
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Image is not found")
public class ImageNotFoundException extends NotFoundException{
    public ImageNotFoundException(final String message) {
        super(message);
    }
}
