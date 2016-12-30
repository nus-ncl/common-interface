package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */

public interface AnalyticsService {

    /**
     *
     * @param dataId: data id
     * @param resourceId: resource id
     * @param downloadDate: download date
     * @param userId: user id
     * @return data downloads
     */

    DataDownload addDataDownloadRecord(Long dataId, Long resourceId, ZonedDateTime downloadDate, String userId);

    /**
     *
     * @param teamId: team id
     * @param startDate: starting date
     * @param endDate: ending date
     * @return usage in node x hour, or "?"
     */

    String getUsageStatistics(String teamId, String startDate, String endDate);
}
