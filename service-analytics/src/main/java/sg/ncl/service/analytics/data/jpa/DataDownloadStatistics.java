package sg.ncl.service.analytics.data.jpa;

import lombok.Getter;

/**
 * Created by dcsjnh on 12/30/2016.
 *
 * References:
 * [1] http://stackoverflow.com/questions/36328063/how-to-return-a-custom-object-from-a-spring-data-jpa-group-by-query
 */
@Getter
public class DataDownloadStatistics {

    private Long dataId;
    private Long count;

    public DataDownloadStatistics(Long dataId, Long count) {
        this.dataId = dataId;
        this.count = count;
    }

}
