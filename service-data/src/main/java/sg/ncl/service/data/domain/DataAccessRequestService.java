package sg.ncl.service.data.domain;

import io.jsonwebtoken.Claims;

/**
 * Created by dcsjnh on 12/22/2016.
 */
public interface DataAccessRequestService {

    DataAccessRequest createRequest(Long id, String reason, Claims claims);

}
