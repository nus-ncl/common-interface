package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
@FunctionalInterface
public interface AnalyticsService {

    void addDataDownloadRecord(Long id, Long resourceId, Long dataId, ZonedDateTime downloadDate);
}
