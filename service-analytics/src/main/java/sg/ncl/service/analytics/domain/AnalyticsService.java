package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */

public interface AnalyticsService {

    /**
     *
     * @param dataId: data id
     * @param resourceId: resource id
     * @param downloadDate: download date
     * @param userId: user id
     * @return
     */

    DataDownload addDataDownloadRecord(Long dataId, Long resourceId, ZonedDateTime downloadDate, String userId);

    /**
     *
     * @param id: team id
     * @return usage in node x hour, or "?"
     */

    String getUsageStatistics(String startDate, String startEnd,String id );
}
