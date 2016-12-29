package sg.ncl.service.data.domain;

import java.time.ZonedDateTime;

/**
 * Created by dcsjnh on 12/22/2016.
 */
public interface DataAccessRequest {

    Long getId();

    Long getDataId();

    String getRequesterId();

    String getReason();

    ZonedDateTime getRequestDate();

    ZonedDateTime getApprovedDate();

}
