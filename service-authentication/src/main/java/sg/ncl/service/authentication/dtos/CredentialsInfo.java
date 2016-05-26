package sg.ncl.service.authentication.dtos;

import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;

/**
 * @author Christopher Zhong
 */
public class CredentialsInfo implements Credentials {

    private final String username;
    private final String password;
    private final String userId;
    private final CredentialsStatus status;

    public CredentialsInfo(final String username, final String password, final String userId, final CredentialsStatus status) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public CredentialsStatus getStatus() {
        return status;
    }

}
