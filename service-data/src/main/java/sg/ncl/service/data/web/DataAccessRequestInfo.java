package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.DataAccessRequest;

import java.time.ZonedDateTime;

/**
 * Created by dcsjnh on 12/22/2016.
 */
@Getter
public class DataAccessRequestInfo implements DataAccessRequest {

    private Long id;
    private Long dataId;
    private String requesterId;
    private String reason;
    private ZonedDateTime requestDate;
    private ZonedDateTime approvedDate;

    @JsonCreator
    public DataAccessRequestInfo(@JsonProperty("id") final Long id,
                                 @JsonProperty("dataId") final Long dataId,
                                 @JsonProperty("requesterId") final String requesterId,
                                 @JsonProperty("reason") final String reason,
                                 @JsonProperty("requestDate") final ZonedDateTime requestDate,
                                 @JsonProperty("approvedDate") final ZonedDateTime approvedDate) {
        this.id = id;
        this.dataId = dataId;
        this.requesterId = requesterId;
        this.reason = reason;
        this.requestDate = requestDate;
        this.approvedDate = approvedDate;
    }

    public DataAccessRequestInfo(final DataAccessRequest dataAccessRequest) {
        this(
                dataAccessRequest.getId(),
                dataAccessRequest.getDataId(),
                dataAccessRequest.getRequesterId(),
                dataAccessRequest.getReason(),
                dataAccessRequest.getRequestDate(),
                dataAccessRequest.getApprovedDate()
        );
    }

}
