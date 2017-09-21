package sg.ncl.service.analytics.domain;

import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */

public interface AnalyticsService {

    DataPublicDownload addDataPublicDownloadRecord(Long dataId, Long resourceId, ZonedDateTime downloadDate, Long userId);

    List<DataDownloadStatistics> getDataPublicDownloadCount(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate);

    DataDownload addDataDownloadRecord(Long dataId, Long resourceId, ZonedDateTime downloadDate, String userId);

    List<DataDownloadStatistics> getDataDownloadCount(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate);

    String getUsageStatistics(String teamId, ZonedDateTime startDate, ZonedDateTime endDate);

    List<Double> getEnergyStatistics(ZonedDateTime startDate, ZonedDateTime endDate);

}
