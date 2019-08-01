package sg.ncl.service.analytics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author: Tran Ly Vu, James Ng
 */

@Configuration
@ConfigurationProperties(prefix = "ncl.analytics")
@Getter
@Setter
public class AnalyticsProperties {
    private String energyDir;
    private String diskUsageFile;
    private Map<String, String> diskSpaceThreshold;
}
