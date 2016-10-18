package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.DataStatistics;

import java.time.ZonedDateTime;

/**
 * Created by jng on 17/10/16.
 */
@Getter
public class DataStatisticsInfo implements DataStatistics {

    private Long id;
    private String userId;
    private ZonedDateTime date;

    @JsonCreator
    public DataStatisticsInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("userId") final String userId,
            @JsonProperty("date") final ZonedDateTime date
    ) {
        this.id = id;
        this.userId = userId;
        this.date = date;
    }

    public DataStatisticsInfo(final DataStatistics dataStatistics) {
        this(
                dataStatistics.getId(),
                dataStatistics.getUserId(),
                dataStatistics.getDate()
        );
    }
}
