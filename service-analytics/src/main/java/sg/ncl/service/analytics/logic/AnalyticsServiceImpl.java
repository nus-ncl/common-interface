package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.service.analytics.domain.AnalyticsService;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
@Service
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    public void addDataDownloadRecord(Long id, Long resourceId, Long Userid, ZonedDateTime date) {

    }

}