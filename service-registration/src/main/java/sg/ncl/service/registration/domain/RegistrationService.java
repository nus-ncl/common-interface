package sg.ncl.service.registration.domain;

import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.user.domain.User;

/**
 * @author Christopher Zhong
 */
public interface RegistrationService {


    void registerRequestToJoinTeam(String uid, Team team);

    void register(Credentials credentials, User user, Team team, boolean isJoinTeam);


}
