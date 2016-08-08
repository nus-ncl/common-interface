package sg.ncl.service.authentication.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.authentication.domain.Credentials;
import sg.ncl.service.authentication.domain.CredentialsStatus;

/**
 * @author Christopher Zhong
 */
@Getter
public class CredentialsInfo implements Credentials {

    private final String id;
    private final String username;
    private final String password;
    private final CredentialsStatus status;

    @JsonCreator
    public CredentialsInfo(@JsonProperty("id") final String id, @JsonProperty("username") final String username, @JsonProperty("password") final String password, @JsonProperty("status") final CredentialsStatus status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public CredentialsInfo(final Credentials credentials) {
        this(credentials.getId(), credentials.getUsername(), "*****", credentials.getStatus());
    }

}
