package sg.ncl.service.realization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Desmond.
 */
@Component
@ConfigurationProperties(prefix = "deterurl")
@Getter
@Setter
// FIXME: this should be in the adapter-deterlab project
public class RealizationConnectionProperties {
    private String bossurl;
    private String userurl;
    private String port;
}
