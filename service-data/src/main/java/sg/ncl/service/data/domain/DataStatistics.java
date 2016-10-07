package sg.ncl.service.data.domain;

import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 10/7/2016.
 */
public interface DataStatistics {

    Long getId();

    String getUserId();

    ZonedDateTime getDate();
}
