package sg.ncl.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author teye
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "ncl.av-scanner")
public class AvScannerProperties {
    private String cron;
    private String host;
    private Integer port;
}
