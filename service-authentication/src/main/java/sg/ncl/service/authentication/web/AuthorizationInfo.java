package sg.ncl.service.authentication.web;

import sg.ncl.service.authentication.domain.Authorization;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public class AuthorizationInfo implements Authorization {

    private final String id;
    private final String token;

    public AuthorizationInfo(final String id, final String token) {
        this.id = id;
        this.token = token;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getToken() {
        return token;
    }

}
