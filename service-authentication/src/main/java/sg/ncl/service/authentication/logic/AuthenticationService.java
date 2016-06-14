package sg.ncl.service.authentication.logic;

import sg.ncl.service.authentication.domain.Authorization;

/**
 * @author Christopher Zhong
 */
public interface AuthenticationService {

    Authorization login(String username, String password);

}
