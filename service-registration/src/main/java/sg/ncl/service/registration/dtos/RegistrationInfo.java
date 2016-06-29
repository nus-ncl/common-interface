package sg.ncl.service.registration.dtos;

import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.user.data.jpa.entities.UserEntity;

/**
 * @author Christopher Zhong
 */
public class RegistrationInfo {

    // FIXME Credentials cannot deserialize properly but CredentialsEntity can
    // FIXME Cannot use interface; Only Entities have the toString()
    private CredentialsEntity credentials;
    private UserEntity user;
    private TeamEntity team;
    private boolean isJoinTeam;

    public CredentialsEntity getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsEntity credentials) {
        this.credentials = credentials;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    public boolean getIsJoinTeam() {
        return isJoinTeam;
    }

    public void setJoinTeam(boolean joinTeam) {
        isJoinTeam = joinTeam;
    }
}
