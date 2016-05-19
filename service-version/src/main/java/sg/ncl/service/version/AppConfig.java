package sg.ncl.service.version;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import sg.ncl.service.version.domain.VersionInfo;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.version.AppConfig")
public class AppConfig {

    @Bean
    @Scope("singleton")
    VersionInfo versionInfo() {
        return new VersionInfo(1, 0, "3ade5", ZonedDateTime.now());
    }

}
