package sg.ncl.service.registration.domain;

import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.team.domain.Team;
import sg.ncl.service.team.domain.TeamStatus;
import sg.ncl.service.user.domain.User;

/**
 * @author Christopher Zhong, Tran Ly vu
 */
public interface RegistrationService {

    Registration registerRequestToApplyTeam(String uid, Team team);

    Registration registerRequestToJoinTeam(String uid, Team team);

    Registration register(Credentials credentials, User user, Team team, boolean isJoinTeam);

    String approveJoinRequest(String teamId, String userId, User user);

    String rejectJoinRequest(String teamId, String userId, User user);

    String approveOrRejectNewTeam(String teamId, String ownerId, TeamStatus teamStatus, String reason);

    String getDeterUid(String id);

    boolean verifyEmail(final String uid, final String email, final String key);
}
