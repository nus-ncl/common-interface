package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class NodeUsageInfo {

    private Long id;
    private Long projectId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer noNodes;

    public NodeUsageInfo(Long id,ZonedDateTime startDate, ZonedDateTime endDate, Integer noNodes) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.noNodes = noNodes;
    }


    @Override
    public String toString() {
        return "NodeUsageInfo{id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", noNodes=" + noNodes + "}";
    }
}
