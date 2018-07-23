package sg.ncl.service.analytics.data.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class TeamUsage {
    private static final String ZONE = "Asia/Singapore";
    
    private String exptIdx;
    private ZonedDateTime swapIn;
    private ZonedDateTime swapOut;
    private Integer pnodes;

    public void setSwapIn(String datetime) {
        if (datetime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            swapIn = ZonedDateTime.parse(datetime, formatter.withZone(ZoneId.of(ZONE)));
        }
    }

    public void setSwapOut(String datetime) {
        if (datetime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            swapOut = ZonedDateTime.parse(datetime, formatter.withZone(ZoneId.of(ZONE)));
        }
    }

    public void computeNodeUsageByDay(Map<String, Long> dayUsage, ZonedDateTime dayEnd) {
        ZonedDateTime cutoff;
        if (swapOut != null) {
            cutoff = swapOut;
        } else {
            cutoff = dayEnd.plusDays(1);
        }

        computeNodeUsage(dayUsage, cutoff);
    }

    private void computeNodeUsage(Map<String, Long> dayUsage, ZonedDateTime cutoff) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (swapIn.getYear() == cutoff.getYear() && swapIn.getDayOfYear() == cutoff.getDayOfYear()) {
            String key = formatter.format(swapIn);
            Long value = dayUsage.get(key) == null ? 0L : dayUsage.get(key);
            dayUsage.put(key , value + (Duration.between(swapIn, cutoff).toMinutes() * pnodes));
        } else {
            ZonedDateTime today = ZonedDateTime.of(
                    swapIn.getYear(), swapIn.getMonthValue(), swapIn.getDayOfMonth(),
                    0, 0, 0, 0, ZoneId.of(ZONE));
            ZonedDateTime tomorrow = today.plusDays(1);
            String key = formatter.format(swapIn);
            Long value = dayUsage.get(key) == null ? 0L : dayUsage.get(key);
            dayUsage.put(key, value + (Duration.between(swapIn, tomorrow).toMinutes() * pnodes));
            today = tomorrow;
            tomorrow = tomorrow.plusDays(1);

            while (tomorrow.isBefore(cutoff)) {
                key = formatter.format(today);
                value = dayUsage.get(key) == null ? 0L : dayUsage.get(key);
                dayUsage.put(key, value + (1440L * pnodes));
                today = tomorrow;
                tomorrow = tomorrow.plusDays(1);
            }

            key = formatter.format(today);
            value = dayUsage.get(key) == null ? 0L : dayUsage.get(key);
            dayUsage.put(key, value + (Duration.between(today, cutoff).toMinutes() * pnodes));
        }
    }
}
