package sg.ncl.service.transmission.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.ConflictException;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Arbitrary File Write via Path Traversal.")
public class UploadOutOfBaseDirException extends ConflictException{
    public UploadOutOfBaseDirException(final String message) {
        super(message);
    }
}

