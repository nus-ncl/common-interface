package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class NodesReservationEntry {

    private String project;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer noNodes;

    public NodesReservationEntry(String project, ZonedDateTime startDate, ZonedDateTime endDate, Integer noNodes) {
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
        this.noNodes = noNodes;
    }

    public void computeNodesReserveByDay(List<Integer> nodes, ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime current = start;
        int counter = 0;
        while (!current.isAfter(end)) {
            if (counter >= nodes.size()) {
                nodes.add(0);
            }
            if (!(current.isBefore(startDate) || current.isAfter(endDate))) {
                nodes.set(counter, nodes.get(counter) + noNodes);
            }
            current = current.plusDays(1);
            counter++;
        }
    }

    @Override
    public String toString() {
        return "NodesReservationEntry{project=" + project +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", noNodes=" + noNodes + "}";
    }
}
