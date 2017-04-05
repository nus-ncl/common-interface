package sg.ncl.service.analytics.logic;

import lombok.Getter;
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
import java.time.DayOfWeek;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public List<Double> getEnergyStatistics(ZonedDateTime startDate, ZonedDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException();

        //preparation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");  //format into string
        Comparator<ZonedDateTime> comparator = Comparator.comparing(zdt -> zdt.truncatedTo(ChronoUnit.DAYS));  // to compare 2 ZonedTimeDate's
        List<String> filenameList = new ArrayList<>();
        List<String> distinctList = new ArrayList<>();
        List<Energy> energyList = new ArrayList<>();
        List<Double> energyStatistics = new ArrayList<>();

        // The real end date to use is the next day of the end date retrieve from WS
        ZonedDateTime realEndDate = endDate.plusDays(1);
        String start = startDate.format(formatter);
        String end = realEndDate.format(formatter);

        //add all the log inside filenameList
        Path path = Paths.get(System.getProperty("user.home"));
        path = Paths.get(path.getRoot().toString(), analyticsProperties.getEnergyDir());
        File dir = new File(path.toString());
        Pattern p = Pattern.compile("(nclenergy\\.)\\d{12}(\\.out)");

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i =0; i < files.length; i++) {
                if (p.matcher(files[i].getName()).matches()) {
                    filenameList.add(files[i].toString());
                }
            }
        }

        // retrieve only first log of each day (morning) into distinctList
        Collections.sort(filenameList);
        String previous = "";
        for (String filename : filenameList) {
            String[] parts = filename.split("\\.");
            String subString = parts[1].substring(0, 8);
            if (subString.compareTo(start) >= 0 && subString.compareTo(end) <= 0) {
                if (!previous.equals(subString)) {
                    distinctList.add(filename);
                    previous = subString;
                }
            }
        }

        //add up or energy and put into energyList
        for (String distinct : distinctList) {
            try {
                Energy energy = readFile(distinct);
                energyList.add(energy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //sort energy list based on file name
        Collections.sort(energyList, new Comparator<Energy>() {
            @Override
            public int compare(Energy energy1, Energy energy2) {
                return energy1.filename.compareTo(energy2.filename); // Ascending
            }
        });


        //if every files are missing
        if (energyList.isEmpty()) {
            int numberOfDays = (int)ChronoUnit.DAYS.between(startDate, realEndDate);
            for (int i =0; i< numberOfDays; i++) {
                energyStatistics.add(0.00);
                i++;
            }
        }

        //check all the missing files up to the first available file
        if (!energyList.isEmpty()) {
            ZonedDateTime firstDateAvailable = energyList.get(0).getZonedDateTime();   // First date that is available
            while (comparator.compare(startDate, firstDateAvailable) != 0) {
                energyStatistics.add(0.00);
                startDate = startDate.plusDays(1);
            }
        }
        log.info(" energyStatistics size is {}", Integer.toString(energyStatistics.size()));
        // list of energy up to the last file that is available
        for (int i = 0; i < energyList.size() - 1; i++) {
            Energy energyI = energyList.get(i);
            Energy energyIPlus1 = energyList.get(i+1);
            int numberOfDays = (int)ChronoUnit.DAYS.between(energyI.getZonedDateTime(), energyIPlus1.getZonedDateTime());
            double difference =  energyIPlus1.getUsage() - energyI.getUsage();
            double averageValue = difference/numberOfDays;
            for (int j = i; j < numberOfDays; j++) {
                energyStatistics.add(averageValue);
            }
        }

        //check all the missing files starting from the last available files
        if (!energyList.isEmpty()) {
            ZonedDateTime lastDateAvailable = energyList.get(energyList.size() - 1).getZonedDateTime();   // First date that is available
            while (comparator.compare(realEndDate, lastDateAvailable) != 0) {
                energyStatistics.add(0.00);
                lastDateAvailable = lastDateAvailable.plusDays(1);
            }
        }

        return energyStatistics;
    }

    private Energy readFile(String filename) throws IOException {

        Energy energy = new Energy(filename);

        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                String[] parts = line.split(" ");
                String usage = parts[3];
                energy.addUsage(Double.parseDouble(usage));
                line = br.readLine();
            }
            return energy;
        } finally {
            br.close();
        }
    }

    @Getter
    private class Energy {
        private String filename; //nclenergy.YYYYMMDDHHMM.out
        private double usage; //accumulated usage

        public void addUsage(double newUsage) {
            usage += newUsage;
        }

        public Energy(String filename) {
            this.filename = filename;
            this.usage = 0;
        }

        public ZonedDateTime getZonedDateTime() {
            if (filename!= null) {
                String[] parts = filename.split("\\.");
                String subString = parts[1].substring(0, 8);
                String year = subString.substring(0,4);
                String month = subString.substring(4,6);
                String day = subString.substring(6,8);

                return ZonedDateTime.of(
                        Integer.parseInt(year),
                        Integer.parseInt(month),
                        Integer.parseInt(day),
                        0, 0, 0, 0, ZoneId.of("Asia/Singapore"));
            } else
                return null;
        }
    }
}
