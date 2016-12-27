package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;

/**
 * Created by dcsjnh on 12/22/2016.
 */
public interface DataAccessRequestService {

    /**
     * Add a dataset access request entry into database.
     *
     * @param did       Dataset Id
     * @param reason    Request Reason
     * @param claims    Authenticated User
     * @return          DataAccessRequestEntity
     */
    DataAccessRequest createRequest(Long did, String reason, Claims claims);

    /**
     * Approve a dataset access request entry.
     *
     * @param rid       Request Id
     * @param claims    Authenticated User
     * @return          DataAccessRequestEntity
     */
    DataAccessRequest approveRequest(Long rid, Claims claims);

    /**
     * Get a dataset access request entry.
     *
     * @param rid       Request Id
     * @param claims    Authenticated User
     * @return          DataAccessRequestEntity
     */
    DataAccessRequest getRequest(Long rid, Claims claims);

}
