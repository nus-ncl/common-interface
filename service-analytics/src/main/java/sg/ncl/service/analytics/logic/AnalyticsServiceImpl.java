package sg.ncl.service.analytics.logic;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import sg.ncl.adapter.deterlab.AdapterDeterLab;
import sg.ncl.common.exception.base.NotFoundException;
import sg.ncl.service.analytics.AnalyticsProperties;
import sg.ncl.service.analytics.data.jpa.*;
import sg.ncl.service.analytics.data.pojo.DiskSpace;
import sg.ncl.service.analytics.data.pojo.TeamUsage;
import sg.ncl.service.analytics.domain.AnalyticsService;
import sg.ncl.service.analytics.domain.DataDownload;
import sg.ncl.service.analytics.domain.DataPublicDownload;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */
@Service
@Slf4j
@EnableConfigurationProperties(AnalyticsProperties.class)
public class AnalyticsServiceImpl implements AnalyticsService {
    private static final String EXPTIDX = "exptidx";

    private final DataPublicDownloadRepository dataPublicDownloadRepository;

    private final DataDownloadRepository dataDownloadRepository;

    private final AdapterDeterLab adapterDeterLab;

    private final AnalyticsProperties analyticsProperties;

    public AnalyticsServiceImpl( ) {
        dataPublicDownloadRepository = null;
        dataDownloadRepository = null;
        adapterDeterLab = null;
        analyticsProperties = null;
    }

    @Inject
    AnalyticsServiceImpl(@NotNull DataPublicDownloadRepository dataPublicDownloadRepository,
                         @NotNull DataDownloadRepository dataDownloadRepository,
                         @NotNull final AdapterDeterLab adapterDeterLab,
                         @NotNull final AnalyticsProperties analyticsProperties) {
        this.dataPublicDownloadRepository = dataPublicDownloadRepository;
        this.dataDownloadRepository = dataDownloadRepository;
        this.adapterDeterLab = adapterDeterLab;
        this.analyticsProperties = analyticsProperties;
    }

    @Override
    public DataPublicDownload addDataPublicDownloadRecord(Long dataId, Long resourceId, ZonedDateTime date, Long userId) {
        DataPublicDownloadEntity entity = new DataPublicDownloadEntity();
        entity.setDataId(dataId);
        entity.setResourceId(resourceId);
        entity.setDownloadDate(date);
        entity.setPublicUserId(userId);

        DataPublicDownloadEntity savedEntity = dataPublicDownloadRepository.save(entity);
        log.info("New public open data download record saved: {}", savedEntity.toString());

        return savedEntity;
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
    public List<DataDownloadStatistics> getDataPublicDownloadCount(Long dataId, ZonedDateTime startDate, ZonedDateTime endDate) {
        List<DataDownloadStatistics> statisticsList;
        switch (getFlags(dataId, startDate, endDate)) {
            case 111:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDataIdAndDownloadDateBetween(dataId, startDate, endDate);
                break;
            case 110:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDataIdAndDownloadDateAfter(dataId, startDate);
                break;
            case 101:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDataIdAndDownloadDateBefore(dataId, endDate);
                break;
            case 100:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDataId(dataId);
                break;
            case 11:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDownloadDateBetween(startDate, endDate);
                break;
            case 10:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDownloadDateAfter(startDate);
                break;
            case 1:
                statisticsList = dataPublicDownloadRepository.findDownloadCountByDownloadDateBefore(endDate);
                break;
            default:
                statisticsList = dataPublicDownloadRepository.findDownloadCount();
        }
        return statisticsList;
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

    // no longer in use; it has been replaced by getTeamExptStats() below
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

    /**
     * Get a team's daily usage for a given period from startDate to endDate (both inclusive)
     * @param teamId
     * @param startDate e.g., 2018-08-01T00:00+08:00[Asia/Singapore]
     * @param endDate e.g., 2018-08-14T00:00+08:00[Asia/Singapore], inclusive
     * @return daily usage in node-minutes
     */
    @Override
    public List<Long> getTeamUsage(String teamId, ZonedDateTime startDate, ZonedDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException();

        log.info("Getting usage stats for {} from {} to {}", teamId, startDate, endDate);

        List<TeamUsage> usageList = new ArrayList<>();
        Map<Integer, TeamUsage> usages = new HashMap<>();
        String jsonString = adapterDeterLab.getTeamExptStats(teamId);
        JSONObject jsonObject = new JSONObject(jsonString);

        for (int i = 1; i < jsonObject.length(); i++) {
            JSONObject object = jsonObject.getJSONObject(Integer.toString(i));
            String action = object.getString("action");
            if (action.equals("swapin") || action.equals("start")) {
                TeamUsage usage = new TeamUsage();
                usage.setExptIdx(object.getInt(EXPTIDX));
                usage.setSwapIn(object.getString("start_time"));
                usage.setPnodes(object.getInt("pnodes"));
                usages.put(object.getInt(EXPTIDX), usage);
                usageList.add(usage);
            } else if (action.equals("swapout")) {
                TeamUsage usage = usages.get(object.getInt(EXPTIDX));
                if (usage != null) {
                    usage.setSwapOut(object.getString("start_time"));
                }
            }
        }

        usageList.removeIf( u -> u.getSwapOut() != null && u.getSwapOut().isBefore(startDate) );
        Map<String, Long> dayUsage = new HashMap<>();
        usageList.forEach( usage -> usage.computeNodeUsageByDay(dayUsage, endDate) );
        List<Long> nodeUsage = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZonedDateTime currentDate = startDate.plusDays(0);
        while (currentDate.isBefore(endDate)) {
            Long value = dayUsage.get(formatter.format(currentDate));
            nodeUsage.add(value == null ? 0 : value);
            currentDate = currentDate.plusDays(1);
        }
        ZonedDateTime nowDate = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (currentDate.isBefore(nowDate)) {
            Long value = dayUsage.get(formatter.format(currentDate));
            nodeUsage.add(value == null ? 0 : value);
        } else {
            nodeUsage.add(0L);
        }

        return nodeUsage;
    }

    @Override
    public List<Double> getEnergyStatistics(ZonedDateTime startDate, ZonedDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException();

        //preparation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");  //format into string
        Comparator<ZonedDateTime> comparator = Comparator.comparing(zdt -> zdt.truncatedTo(ChronoUnit.DAYS));  // to compare 2 ZonedTimeDate's
        List<Double> energyStatistics = new ArrayList<>();

        // The real end date to use is the next day of the end date retrieve from WS
        ZonedDateTime realEndDate = endDate.plusDays(1);
        String start = startDate.format(formatter);
        String end = realEndDate.format(formatter);

        List<Energy> energyList = getEnergyList(start, end);

        //if every files are missing
        if (energyList == null || energyList.isEmpty()) {
            int numberOfDays = (int)ChronoUnit.DAYS.between(startDate, realEndDate);
            for (int i =0; i< numberOfDays; i++) {
                energyStatistics.add(0.00);
            }
        } else {
            //sort energy list based on file name
            energyList.sort(
                    (Energy energy1, Energy energy2) -> energy1.filename.compareTo(energy2.filename)
            );

            //check all the missing files up to the first available file
            ZonedDateTime dateToStartCount = startDate;
            ZonedDateTime firstDateAvailable = energyList.get(0).getZonedDateTime();   // First available date
            while (comparator.compare(dateToStartCount, firstDateAvailable) != 0) {
                energyStatistics.add(0.00);
                dateToStartCount = dateToStartCount.plusDays(1);
            }

            // list of energy up to the last file that is available
            for (int i = 0; i < energyList.size() - 1; i++) {
                Energy energyI = energyList.get(i);
                Energy energyIPlus1 = energyList.get(i + 1);
                int numberOfDays = (int) ChronoUnit.DAYS.between(energyI.getZonedDateTime(), energyIPlus1.getZonedDateTime());
                double difference = energyIPlus1.getUsage() - energyI.getUsage();
                double averageValue = difference / numberOfDays;
                for (int j = 0; j < numberOfDays; j++) {
                    energyStatistics.add(averageValue);
                }
            }

            //check all the missing files starting from the last available files
            ZonedDateTime lastDateAvailable = energyList.get(energyList.size() - 1).getZonedDateTime();   // last available date
            while (comparator.compare(realEndDate, lastDateAvailable) != 0) {
                energyStatistics.add(0.00);
                lastDateAvailable = lastDateAvailable.plusDays(1);
            }

        }
        log.info("Getting energy statistics : number of date retrieve is {} ", energyStatistics.size());
        return energyStatistics;
    }

    private List<Energy> getEnergyList(String start, String end){

        List<String> filenameList = new ArrayList<>();
        List<String> distinctList = new ArrayList<>();
        List<Energy> energyList = new ArrayList<>();

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
            if (subString.compareTo(start) >= 0 && subString.compareTo(end) <= 0 && !previous.equals(subString)) {
                distinctList.add(filename);
                previous = subString;
            }
        }

        //add up or energy and put into energyList
        for (String distinct : distinctList) {
            try {
                Energy energy = readFile(distinct);
                energyList.add(energy);
            } catch (IOException e) {
                log.error("Getting energy statistics: {}", e);
                e.printStackTrace();
            }
        }
        return  energyList;
    }

    private Energy readFile(String filename) throws IOException {

        Energy energy = new Energy(filename);

        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
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
    @Setter
    static class Energy {
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

    @Override
    public Map<String, Map<String, List<DiskSpace>>> getDiskStatistics() {
        Map<String, Map<String, List<DiskSpace>>> statistics = new HashMap<>();
        Map<String, List<DiskSpace>> diskSpaces = new HashMap<>();
        List<DiskSpace> userSpaces = new ArrayList<>();
        List<DiskSpace> projSpaces = new ArrayList<>();
        Path path = Paths.get(System.getProperty("user.home"));
        File file = new File(Paths.get(path.getRoot().toString(), analyticsProperties.getDiskUsageFile()).toString());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String timestamp;
            if ((timestamp = br.readLine()) != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] splitted = StringUtils.split(line);
                        if (splitted[1].contains("/big/users/")) {
                            String deterUserId = StringUtils.substringAfter(splitted[1], "/big/users/");
                            try {
                                String nclUserId = adapterDeterLab.getNclUserIdByDeterUserId(deterUserId);
                                userSpaces.add(new DiskSpace(splitted[0], splitted[1], nclUserId));
                            } catch (Exception e) {
                                userSpaces.add(new DiskSpace(splitted[0], splitted[1]));
                            }
                        } else if (splitted[1].contains("/big/proj/")) {
                            String deterProjectId = StringUtils.substringAfter(splitted[1], "/big/proj/");
                            try {
                                String nclTeamId = adapterDeterLab.getNclTeamIdByDeterProjectId(deterProjectId);
                                projSpaces.add(new DiskSpace(splitted[0], splitted[1], nclTeamId));
                            } catch (Exception e) {
                                projSpaces.add(new DiskSpace(splitted[0], splitted[1]));
                            }
                        }
                    }
                }
                diskSpaces.put("userSpaces", userSpaces);
                diskSpaces.put("projSpaces", projSpaces);
            }
            statistics.put(timestamp, diskSpaces);
        } catch (IOException ioe) {
            log.error(ioe.toString());
            throw new NotFoundException();
        }
        return statistics;
    }

    @Override
    public DiskSpace getUserDiskUsage(String userId) {
        DiskSpace diskSpace = null;
        String deterUserId = adapterDeterLab.getDeterUserIdByNclUserId(userId);
        Path path = Paths.get(System.getProperty("user.home"));
        File file = new File(Paths.get(path.getRoot().toString(), analyticsProperties.getDiskUsageFile()).toString());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] splitted = StringUtils.split(line);
                    if (splitted[1].contains(deterUserId)) {
                        diskSpace = new DiskSpace(splitted[0], splitted[1], userId);
                        diskSpace.setAlert(determineAlert(splitted[0]));
                        diskSpace.setQuota(analyticsProperties.getDiskSpaceThreshold().get("danger"));
                        break;
                    }
                }
            }
        } catch (IOException ioe) {
            log.error(ioe.toString());
            throw new NotFoundException();
        }
        return diskSpace;
    }

    private String determineAlert(String usage) {
        String alert = "info";
        double usageSize = parseDiskSize(usage);
        double dangerSize = parseDiskSize(analyticsProperties.getDiskSpaceThreshold().get("danger"));
        double warningSize = parseDiskSize(analyticsProperties.getDiskSpaceThreshold().get("warning"));
        if (usageSize > dangerSize) {
            alert = "danger";
        } else if (usageSize > warningSize) {
            alert = "warning";
        }
        return alert;
    }

    private double parseDiskSize(String sizeStr) {
        double size = Double.parseDouble(sizeStr.replaceFirst(".$",""));
        if (sizeStr.endsWith("G")) {
            size = size * 1000 * 1000;
        } else if (sizeStr.endsWith("M")) {
            size = size * 1000;
        }
        return size;
    }
}
