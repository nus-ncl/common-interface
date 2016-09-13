package sg.ncl.service.team.exceptions;

import sg.ncl.common.exception.base.NotFoundException;

/**
 * Created by Desmond / Te Ye
 */
public class TeamNotFoundException extends NotFoundException {

    public TeamNotFoundException(String id) {
        super(id);
    }

    public TeamNotFoundException() {
        super("Team not found");
    }

}
