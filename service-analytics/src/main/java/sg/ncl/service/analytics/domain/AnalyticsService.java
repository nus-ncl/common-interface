package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
public interface AnalyticsService {

    void addDataDownloadRecord(Long id, Long resourceId, Long Dataid, ZonedDateTime downloadDate);
}
