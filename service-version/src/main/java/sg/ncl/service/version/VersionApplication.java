package sg.ncl.service.version;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sg.ncl.service.version.domain.Version;
import sg.ncl.service.version.web.VersionInfo;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
public class VersionApplication {

    @Bean
    Version versionInfo() {
        return new VersionInfo(1, 0, "3ade5", ZonedDateTime.now());
    }

    public static void main(final String[] args) {
        SpringApplication.run(VersionApplication.class, args);
    }

}
