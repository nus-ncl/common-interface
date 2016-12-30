package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.DataDownload;
import sg.ncl.adapter.deterlab.AdapterDeterLab;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */
@Service
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final DataDownloadRepository dataDownloadRepository;

    private final AdapterDeterLab adapterDeterLab;

    @Inject
    AnalyticsServiceImpl(@NotNull DataDownloadRepository dataDownloadRepository, @NotNull final AdapterDeterLab adapterDeterLab) {
        this.dataDownloadRepository = dataDownloadRepository;
        this.adapterDeterLab = adapterDeterLab;

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
    public String getUsageStatistics(String teamId, String startDate, String endDate) {
        log.info("Getting usage statistics for team {}, start {}, end {}", teamId, startDate, endDate);
        return adapterDeterLab.getUsageStatistics(teamId, startDate, endDate);

    }

}
