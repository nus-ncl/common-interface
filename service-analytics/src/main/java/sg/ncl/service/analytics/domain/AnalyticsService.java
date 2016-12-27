package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
@FunctionalInterface
public interface AnalyticsService {

    void addDataDownloadRecord( Long dataId, Long resourceId, ZonedDateTime downloadDate, String userId);
}
