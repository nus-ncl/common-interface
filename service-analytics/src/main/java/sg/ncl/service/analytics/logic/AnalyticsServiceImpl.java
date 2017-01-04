package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.DataDownload;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author: Tran Ly Vu
 */
@Service
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final DataDownloadRepository dataDownloadRepository;

    @Inject
    AnalyticsServiceImpl(@NotNull DataDownloadRepository dataDownloadRepository) {
        this.dataDownloadRepository = dataDownloadRepository;
    }

    @Override
    public DataDownload addDataDownloadRecord(Long dataId, Long resourceId, ZonedDateTime date, String userId) {
        DataDownloadEntity entity = new DataDownloadEntity();
        entity.setDataId(dataId);
        entity.setResourceId(resourceId);
        entity.setDownloadDate(date);
        entity.setHashedUserId(userId);

        DataDownloadEntity savedEntity = dataDownloadRepository.save(entity);
        log.info("New data download record saved: {}", savedEntity.toString());

        return savedEntity;
    }

    @Override
    public List<DataDownloadStatistics> getDataDownloadCount(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate) {
        List<DataDownloadStatistics> statisticsList;
        int flags = 0;
        if (dataId != null) flags += 100;
        if (startDate != null) flags += 10;
        if (endDate != null) flags += 1;
        switch (flags) {
            case 111:
                statisticsList = dataDownloadRepository.findDownloadCountByDataIdAndDownloadDateBetween(dataId, startDate, endDate);
                break;
            case 110:
                statisticsList = dataDownloadRepository.findDownloadCountByDataIdAndDownloadDateAfter(dataId, startDate);
                break;
            case 101:
                statisticsList = dataDownloadRepository.findDownloadCountByDataIdAndDownloadDateBefore(dataId, endDate);
                break;
            case 100:
                statisticsList = dataDownloadRepository.findDownloadCountByDataId(dataId);
                break;
            case 11:
                statisticsList = dataDownloadRepository.findDownloadCountByDownloadDateBetween(startDate, endDate);
                break;
            case 10:
                statisticsList = dataDownloadRepository.findDownloadCountByDownloadDateAfter(startDate);
                break;
            case 1:
                statisticsList = dataDownloadRepository.findDownloadCountByDownloadDateBefore(endDate);
                break;
            default:
                statisticsList = dataDownloadRepository.findDownloadCount();
        }
        return statisticsList;
    }

}
