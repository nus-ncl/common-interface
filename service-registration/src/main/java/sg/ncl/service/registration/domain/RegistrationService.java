package sg.ncl.service.registration.domain;

import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.user.domain.User;

/**
 * @author Christopher Zhong
 */
public interface RegistrationService {

    void registerRequestToApplyTeam(String uid, Team team);

    String registerRequestToJoinTeam(String uid, Team team);

    void register(Credentials credentials, User user, Team team, boolean isJoinTeam);

    void approveJoinRequest(String teamId, String userId, User user);

    void rejectJoinRequest(String teamId, String userId, User user);

    void approveTeam(String teamId, String ownerId, TeamStatus teamStatus);

    String getDeterUid(String id);

    void activateAccount(String uid, String key);
}
