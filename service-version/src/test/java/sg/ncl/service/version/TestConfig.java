package sg.ncl.service.version;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import sg.ncl.service.version.domain.Version;
import sg.ncl.service.version.web.VersionInfo;

import java.time.ZonedDateTime;

/**
 * Created by chris on 8/22/2016.
 */
@TestConfiguration
public class TestConfig {

    @Bean
    Version version() {
        return new VersionInfo(1, 2, "build", ZonedDateTime.now());
    }

}
