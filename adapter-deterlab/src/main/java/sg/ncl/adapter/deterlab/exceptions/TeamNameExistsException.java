package sg.ncl.adapter.deterlab.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sg.ncl.common.exception.base.BadRequestException;
/**
 * Created by Tran Ly Vu on 11/21/2016.
 */

@ResponseStatus (value = HttpStatus.BAD_REQUEST, reason = "Team Name is already in use")
public class TeamNameExistsException  extends BadRequestException{

    public TeamNameExistsException(){
        super("Team Name is already in use");
    }
}
