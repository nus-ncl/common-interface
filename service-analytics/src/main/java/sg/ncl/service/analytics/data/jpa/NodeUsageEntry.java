package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class NodeUsageEntry {

    private Long id;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer noNodes;

    public NodeUsageEntry(Long id, ZonedDateTime startDate, ZonedDateTime endDate, Integer noNodes) {
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
