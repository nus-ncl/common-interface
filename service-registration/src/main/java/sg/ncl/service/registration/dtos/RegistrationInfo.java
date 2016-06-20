package sg.ncl.service.registration.dtos;

import sg.ncl.service.authentication.data.jpa.CredentialsEntity;
import sg.ncl.service.registration.data.jpa.entities.RegistrationEntity;
import sg.ncl.service.registration.domain.Registration;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;
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
    private RegistrationEntity registration;

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

    public RegistrationEntity getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationEntity registration) {
        this.registration = registration;
    }
}
