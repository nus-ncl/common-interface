package sg.ncl.service.authentication.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.authentication.domain.Authorization;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Getter
public class AuthorizationInfo implements Authorization {

    private final String id;
    private final String token;

    @JsonCreator
    public AuthorizationInfo(@JsonProperty("id") final String id, @JsonProperty("token") final String token) {
        this.id = id;
        this.token = token;
    }

}
