package sg.ncl.service.analytics.domain;

import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author: Tran Ly Vu
 */
public interface AnalyticsService {

    DataDownload addDataDownloadRecord(Long dataId, Long resourceId, ZonedDateTime downloadDate, String userId);

    List<DataDownloadStatistics> getDataDownloadCount(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate);

}
