package sg.ncl.service.experiment.logic;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 11/15/2017.
 *
 * Represent a realized experiment with status and node details information
 *
 * This class is going to replace the Realization class in Realization service
 */
@Getter
@Setter
public class RealizedExperiment {

    private String teamId;
    private String teamName;
    private Long expId;
    private String expName;
    private String userId;
    private String description;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
    private String state;
    private Integer nodes = 0;
    private Integer minNodes = 0;
    private Long idleHours = 0L;
    private String details;

    @Override
    public String toString() {
        return "RealizedExperiment{" +
                "teamId='" + teamId + "'" +
                ", teamName='" + teamName + "'" +
                ", expId='" + expId + "'" +
                ", expName='" + expName + "'" +
                ", userId='" + userId + "'" +
                ", description='" + description + "'" +
                ", createdDate='" + createdDate + "'" +
                ", lastModifiedDate='" + lastModifiedDate + "'" +
                ", state='" + state + "'" +
                ", nodes='" + nodes + "'" +
                ", minNodes='" + minNodes + "'" +
                ", idleHours='" + idleHours + "'" +
                ", details='" + details + "'}";
    }
}
