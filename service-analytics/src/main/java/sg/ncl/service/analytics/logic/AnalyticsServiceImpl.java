package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.DataDownload;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

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

}
