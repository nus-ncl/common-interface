package sg.ncl.service.authentication.domain;

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

}
