package sg.ncl.service.authentication.domain;

import sg.ncl.common.authentication.Role;

import java.util.Set;

/**
 * Interface that contains the necessary authorization for a user.
 *
 * @author Christopher Zhong
 * @version 1.0
 */
public interface Authorization {

    /**
     * Returns the id of the user.
     *
     * @return the id of the user.
     */
    String getId();

    /**
     * Returns the JWT.
     *
     * @return the JWT.
     */
    String getToken();

    /**
     * Returns the set of {@link Role}s.
     *
     * @return the set of {@link Role}s.
     */
    Set<Role> getRoles();

}
