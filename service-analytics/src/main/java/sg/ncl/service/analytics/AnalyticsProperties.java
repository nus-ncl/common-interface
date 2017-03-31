package sg.ncl.service.analytics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by workshop on 3/31/2017.
 */

@Configuration
@ConfigurationProperties(prefix = "ncl.analytics")
@Getter
@Setter
public class AnalyticsProperties {
    private String energyDir;

}
