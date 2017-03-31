package sg.ncl.service.analytics.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.AccountingProperties;
import sg.ncl.common.exception.base.BadRequestException;
import sg.ncl.service.analytics.AnalyticsProperties;
import sg.ncl.service.analytics.data.jpa.DataDownloadEntity;
import sg.ncl.service.analytics.data.jpa.DataDownloadRepository;
import sg.ncl.service.analytics.data.jpa.DataDownloadStatistics;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.DataDownload;
import sg.ncl.service.analytics.exceptions.StartDateAfterEndDateException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */
@Service
@Slf4j
@EnableConfigurationProperties(AnalyticsProperties.class)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final DataDownloadRepository dataDownloadRepository;

    private final AdapterDeterLab adapterDeterLab;

    private final AnalyticsProperties analyticsProperties;

    @Inject
    AnalyticsServiceImpl(@NotNull DataDownloadRepository dataDownloadRepository, @NotNull final AdapterDeterLab adapterDeterLab, @NotNull final AnalyticsProperties analyticsProperties) {
        this.dataDownloadRepository = dataDownloadRepository;
        this.adapterDeterLab = adapterDeterLab;
        this.analyticsProperties = analyticsProperties;
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
        if (startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String start = startDate.format(formatter);
        String end = endDate.format(formatter);
        log.info("Getting usage statistics for team {}, start {}, end {}", teamId, start, end);
        return adapterDeterLab.getUsageStatistics(teamId, start, end);
    }

    @Override
    public double[] getEnergyStatistics(ZonedDateTime startDate, ZonedDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException();

        Path path = Paths.get(System.getProperty("user.home"));
        path = Paths.get(path.getRoot().toString(), analyticsProperties.getEnergyDir());
        File dir = new File(path.toString());

        Pattern p = Pattern.compile("(nclenergy\\.)\\d{12}(\\.out)");
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i =0; i < files.length; i++) {
                if (p.matcher(files[i].getName()).matches()) {
                   try {
                       log.info(readFile(files[i].toString()));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                }
            }
        }
        return null;
    }

    private String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    private class Energy {
        private String filename;
        private double usage; //accumulated usage

        public double calculateUsage() {


            return null;
        }

    }
}
