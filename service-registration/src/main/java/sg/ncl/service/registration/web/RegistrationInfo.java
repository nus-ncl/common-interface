package sg.ncl.service.registration.web;

import lombok.Getter;
import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.team.data.jpa.TeamEntity;
import sg.ncl.service.user.data.jpa.UserEntity;

/**
 * @author Christopher Zhong
 */
// FIXME: this class is not inline with the Registration interface
@Getter
public class RegistrationInfo {

    // FIXME Credentials cannot deserialize properly but CredentialsEntity can
    // FIXME Cannot use interface; Only Entities have the toString()
    private CredentialsEntity credentials;
    private UserEntity user;
    private TeamEntity team;
    private boolean isJoinTeam;
    // for joining and apply teams after logged on
    private String uid;
    private String notes;

    public void setCredentials(CredentialsEntity credentials) {
        this.credentials = credentials;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNotes(String reason) {
        this.notes = notes;
    }
}
