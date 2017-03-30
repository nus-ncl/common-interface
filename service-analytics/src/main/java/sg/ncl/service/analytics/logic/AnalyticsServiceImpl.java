package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.DataDownload;
import sg.ncl.service.analytics.exceptions.StartDateAfterEndDateException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

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
    public List<DataDownloadStatistics> getDataDownloadCount(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate) {
        List<DataDownloadStatistics> statisticsList;
        switch (getFlags(dataId, startDate, endDate)) {
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

    private int getFlags(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate) {
        int flags = 0;
        if (dataId != null)
            flags += 100;
        if (startDate != null)
            flags += 10;
        if (endDate != null)
            flags += 1;
        return flags;
    }

    @Override
    public String getUsageStatistics(String teamId, ZonedDateTime startDate, ZonedDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String start = startDate.format(formatter);
        String end = endDate.format(formatter);
        log.info("Getting usage statistics for team {}, start {}, end {}", teamId, start, end);
        return adapterDeterLab.getUsageStatistics(teamId, start, end);
    }

    @Override
    public double[] getEnergyStatistics(String startDate, String endDate) {
        ZonedDateTime start = getZonedDateTime(startDate);
        ZonedDateTime end = getZonedDateTime(endDate);
        ZonedDateTime now = ZonedDateTime.now();
        if (start == null) {
            start = now.with(firstDayOfMonth());
        }
        if (end == null) {
            end = now.with(lastDayOfMonth());
        }
        if (start.isAfter(end))
            throw new StartDateAfterEndDateException();



        return null;
    }



    /**
     * Get simple ZonedDateTime from date string in the format 'YYYY-MM-DD'.
     *
     * @param date  date string to convert
     * @return      ZonedDateTime of
     */
    private ZonedDateTime getZonedDateTime(String date) {
        if (date != null) {
            String[] result = date.split("-");
            if (result.length != 3) {
                throw new BadRequestException();
            }
            return ZonedDateTime.of(
                    Integer.parseInt(result[0]),
                    Integer.parseInt(result[1]),
                    Integer.parseInt(result[2]),
                    0, 0, 0, 0, ZoneId.of("Asia/Singapore"));
        }
        return null;
    }
}
