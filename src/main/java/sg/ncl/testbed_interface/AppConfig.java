package sg.ncl.testbed_interface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import sg.ncl.testbed_interface.dtos.VersionInfo;

import java.time.LocalDateTime;

/**
 * @author Christopher Zhong
 */
@Configuration
public class AppConfig {

    @Bean
    @Scope("singleton")
    VersionInfo versionInfo() {
        return new VersionInfo(1, 0, "3ade5", LocalDateTime.now());
    }

}
