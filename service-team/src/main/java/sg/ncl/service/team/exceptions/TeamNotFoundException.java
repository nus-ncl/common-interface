package sg.ncl.service.team.exceptions;

import sg.ncl.common.exception.NotFoundException;

/**
 * Created by Desmond / Te Ye
 */
public class TeamNotFoundException extends NotFoundException {

    public TeamNotFoundException(String teamId) {
        super(teamId);
    }

}
