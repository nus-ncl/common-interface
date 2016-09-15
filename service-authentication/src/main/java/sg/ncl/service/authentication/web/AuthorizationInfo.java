package sg.ncl.service.authentication.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.common.authentication.Role;
import sg.ncl.service.authentication.domain.Authorization;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@Getter
public class AuthorizationInfo implements Authorization {

    private final String id;
    private final String token;
    private final Set<Role> roles = new HashSet<>();

    @JsonCreator
    public AuthorizationInfo(@JsonProperty("id") final String id, @JsonProperty("token") final String token, @JsonProperty("roles") final Set<Role> roles) {
        this.id = id;
        this.token = token;
        this.roles.addAll(roles);
    }

}
