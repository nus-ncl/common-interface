package sg.ncl.service.experiment.web;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 11/15/2017.
 *
 * Represent an experiment with status;
 * if the experiment is active, also include the realization details
 *
 * This class is going to replace the Realization class in Realization service
 */
@Getter
@Setter
public class StatefulExperiment {

    private String teamId;
    private String teamName;
    private Long id;
    private String name;
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
        return "StatefulExperiment{" +
                "teamId='" + teamId + "'" +
                ", teamName='" + teamName + "'" +
                ", id='" + id + "'" +
                ", name='" + name + "'" +
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
